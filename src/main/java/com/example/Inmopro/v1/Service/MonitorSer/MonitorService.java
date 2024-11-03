package com.example.Inmopro.v1.Service.MonitorSer;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Dto.Request.RequestMonitor;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Model.Request.Request;
import com.example.Inmopro.v1.Model.Request.RequestStatus;
import com.example.Inmopro.v1.Model.Request.RequestType;
import com.example.Inmopro.v1.Model.Users.Roles;
import com.example.Inmopro.v1.Model.Users.Users;
import com.example.Inmopro.v1.Repository.*;
import com.example.Inmopro.v1.Service.Jwt.JwtService;
import com.example.Inmopro.v1.Service.Mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonitorService {
    @Autowired
    private  RequestRepository requestRepository;
    private final ResourceLoader resourceLoader;
    private final RequestTypeRepository requestTypeRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final FollowUpRequestRepository followUpRequestRepository;
    private final MailService mailService;

    private JwtService jwtService;
    private UsersRepository usersRepository;


    public Optional<Object[]> getAllRequestsByRol(Integer monitorId) {
        return requestRepository.findAllRequestsByRol(monitorId);
    }

    public Optional<Object[]> getRequestByIdAndMonitorId(Integer requestId, Integer monitorId) {
        return requestRepository.findByIdAndMonitorId(requestId, monitorId);
    }


    public RequestResponse create(RequestMonitor request, HttpServletRequest httpRequest) {

        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);
            Optional<Users> userOptional = usersRepository.findByEmail(email);
            Optional<RequestType> requestTypeOptional = requestTypeRepository.findById(request.getRequestType());
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(2);
            Optional<Users> UserOptionalId = usersRepository.findById(request.getIdUser());


            if(userOptional.isPresent() && requestTypeOptional.isPresent() && requestStatusOptional.isPresent() && UserOptionalId.isPresent()) {
                Users userSession = userOptional.get();
                Integer userRoleSession = userSession.getRole().getId();
                String emailRequest = userSession.getEmail();

                Users userRequest = userOptional.get();
                //Integer userRole = userSession.getRole().getId();

                if (userRoleSession == 3){
                    RequestType requestType = requestTypeOptional.get();
                    RequestStatus requestStatus = requestStatusOptional.get();

                    boolean existsInProgressRequest = requestRepository.existsByTenantAndStatusId(userRequest, requestStatus);
                    if (existsInProgressRequest) {
                        return RequestResponse.builder().message("You have a pending request").build();
                    }

                    Request requestEntity = Request.builder()
                            .tenant(userRequest)
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
                        mailService.sendHtmlEmail(emailRequest, "Request created", htmlContent);
                    }catch (IOException e) {
                        e.printStackTrace();
                        return RequestResponse.builder().message("Error loading email template").build();
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }

                    return RequestResponse.builder().message("Request created").build();


                }else {
                        return RequestResponse.builder().message("User invalid").build();
                }

            }
        }

        return RequestResponse.builder().message("Invalid request").build();
    }
}
