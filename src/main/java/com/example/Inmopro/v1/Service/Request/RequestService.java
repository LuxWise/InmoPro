package com.example.Inmopro.v1.Service.Request;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Model.Request.Request;
import com.example.Inmopro.v1.Model.Request.RequestStatus;
import com.example.Inmopro.v1.Model.Request.RequestType;
import com.example.Inmopro.v1.Model.Users.Users;
import com.example.Inmopro.v1.Repository.*;
import com.example.Inmopro.v1.Service.Jwt.JwtService;
import com.example.Inmopro.v1.Service.Mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final ResourceLoader resourceLoader;
    private final FollowUpRequestRepository followUpRequestRepository;
    private final RequestRepository requestRepository;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final MailService mailService;

    public Optional<Object[]> getAllRequests() {
        return requestRepository.findAllRequests();
    }

    public Optional<Object[]> getRequestById(Integer requestId) {
        return requestRepository.findRequestById(requestId);
    }

    public RequestResponse create(RequestRequest request, HttpServletRequest httpRequest) throws IOException, MessagingException {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);
            Optional<Users> userOptional = usersRepository.findByEmail(email);
            Optional<RequestType> requestTypeOptional = requestTypeRepository.findById(request.getRequestType());
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(1);


            if (userOptional.isPresent() && requestTypeOptional.isPresent() && requestStatusOptional.isPresent()) {
                Users users = userOptional.get();
                Integer userRole = users.getRole().getId();
                RequestType requestType = requestTypeOptional.get();
                RequestStatus requestStatus = requestStatusOptional.get();

                boolean existsInProgressRequest = requestRepository.existsByTenantAndStatusId(users, requestStatus);
                if (existsInProgressRequest) {
                    return RequestResponse.builder().message("You have a pending request").build();
                }

                if (userRole != 1) {
                    return RequestResponse.builder().message("User invalid").build();
                }

                Request requestEntity = Request.builder()
                        .tenant(users)
                        .requestTypeId(requestType)
                        .statusId(requestStatus)
                        .description(request.getDescription())
                        .build();

                requestRepository.save(requestEntity);

                FollowUpRequest followUpRequest = FollowUpRequest.builder()
                        .requestId(requestEntity)
                        .statusId(requestStatus)
                        .inForce(true)
                        .build();

                followUpRequestRepository.save(followUpRequest);

                Resource resource = resourceLoader.getResource("classpath:static/RequestMessage.html");
                try (InputStream inputStream = resource.getInputStream()) {
                    String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    mailService.sendHtmlEmail(users.getEmail(), "Request created", htmlContent);
                }

                return RequestResponse.builder().message("Request created").build();

            }
        }

        return RequestResponse.builder().message("Invalid request").build();
    }

    public RequestResponse process(Integer requestProcess, HttpServletRequest httpRequest) throws IOException, MessagingException {
        Optional<Users> userOptional = authenticateUser(httpRequest);
        Optional<Request> requestOptional = requestRepository.findByRequestId(requestProcess);
        Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(2);

        if (userOptional.isPresent() && requestOptional.isPresent() && requestStatusOptional.isPresent()) {
            Users user = userOptional.get();
            Request request = requestOptional.get();
            RequestStatus newStatus = requestStatusOptional.get();

            if (!isUserRole(user, 2)) return RequestResponse.builder().message("User invalid").build();
            if (!isRequestStatus(request, 1)) return RequestResponse.builder().message("Request not pending").build();

            request.setStatusId(newStatus);
            requestRepository.save(request);

            Resource resource = resourceLoader.getResource("classpath:static/RequestProcessedMessage.html");
            try (InputStream inputStream = resource.getInputStream()) {
                String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                mailService.sendHtmlEmail(user.getEmail(), "Request Processed", htmlContent);
            }

            return RequestResponse.builder().message("Request processed").build();
        }
        return RequestResponse.builder().message("Invalid request").build();

    }

    public RequestResponse approve(Integer requestApprove, HttpServletRequest httpRequest) throws IOException, MessagingException {
        Optional<Users> userOptional = authenticateUser(httpRequest);
        Optional<Request> requestOptional = requestRepository.findByRequestId(requestApprove);
        Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(3);

        if (userOptional.isPresent() && requestOptional.isPresent() && requestStatusOptional.isPresent()) {
            Users user = userOptional.get();
            Request request = requestOptional.get();
            RequestStatus newStatus = requestStatusOptional.get();

            if (!isUserRole(user, 3)) return RequestResponse.builder().message("User invalid").build();
            if (!isRequestStatus(request, 2)) return RequestResponse.builder().message("Request not processed").build();

            request.setStatusId(newStatus);
            requestRepository.save(request);

            Resource resource = resourceLoader.getResource("classpath:static/RequestApprovedMessage.html.html");
            try (InputStream inputStream = resource.getInputStream()) {
                String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                mailService.sendHtmlEmail(user.getEmail(), "Request approved", htmlContent);
            }

            return RequestResponse.builder().message("Request Approved").build();
        }
        return RequestResponse.builder().message("Invalid request").build();


    }

    public RequestResponse cancel(Integer requestCancel){
        Optional<Request> requestOptional = requestRepository.findByRequestId(requestCancel);
        Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(4);

        if (requestOptional.isPresent() && requestStatusOptional.isPresent()) {
            Request request = requestOptional.get();
            RequestStatus requestStatus = requestStatusOptional.get();

            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(request.getCreatedAt(), now);


            if (request.getStatusId().getId() == 2 && duration.toHours() < 24) {
                return RequestResponse.builder().message("Request already processed").build();
            }

            request.setStatusId(requestStatus);
            requestRepository.save(request);

            return RequestResponse.builder().message("Request canceled").build();
        }

        return RequestResponse.builder().message("Invalid request").build();
    }

    public RequestResponse onHold(Integer requestOnHold, HttpServletRequest httpRequest) throws IOException, MessagingException {
        Optional<Users> userOptional = authenticateUser(httpRequest);
        Optional<Request> requestOptional = requestRepository.findByRequestId(requestOnHold);
        Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(6);

        if (userOptional.isPresent() && requestOptional.isPresent() && requestStatusOptional.isPresent()) {
            Users user = userOptional.get();
            Request request = requestOptional.get();
            RequestStatus newStatus = requestStatusOptional.get();

            if (!isUserRole(user, 2)) return RequestResponse.builder().message("User invalid").build();
            if (!isRequestStatus(request, 1)) return RequestResponse.builder().message("Request not processed").build();


            request.setStatusId(newStatus);
            requestRepository.save(request);

            return RequestResponse.builder().message("Request on hold").build();
        }
        return RequestResponse.builder().message("Invalid request").build();
    }

    public List<FollowUpRequest> getFollowUpRequest() {
        return followUpRequestRepository.findAll();
    }

    public List<Object[]> getFollowUpRequestsByStatusName(String statusName) {
        return followUpRequestRepository.findByStatusName(statusName);
    }

    private Optional<Users> authenticateUser(HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);
            return usersRepository.findByEmail(email);
        }
        return Optional.empty();
    }


    private boolean isUserRole(Users user, int requiredRoleId) {
        return user.getRole().getId() == requiredRoleId;
    }

    private boolean isRequestStatus(Request request, int statusId) {
        return request.getStatusId().getId() == statusId;
    }


}
