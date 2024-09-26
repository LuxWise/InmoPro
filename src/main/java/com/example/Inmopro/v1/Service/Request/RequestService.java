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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final FollowUpRequestRepository followUpRequestRepository;
    private final RequestRepository requestRepository;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final RequestStatusRepository requestStatusRepository;

    public RequestResponse create(RequestRequest request, HttpServletRequest httpRequest) {
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

                if (requestStatus.getId() == 1 || requestStatus.getId() == 3 || requestStatus.getId() == 5) {
                    return RequestResponse.builder().message("Invalid request status").build();
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

                followUpRequestRepository.findByRequestId(requestEntity).forEach(existingFollowUpRequest -> {
                    existingFollowUpRequest.setInForce(false);
                    followUpRequestRepository.save(existingFollowUpRequest);
                });

                FollowUpRequest followUpRequest = FollowUpRequest.builder()
                        .requestId(requestEntity)
                        .statusId(requestStatus)
                        .inForce(true)
                        .build();

                followUpRequestRepository.save(followUpRequest);

                return RequestResponse.builder().message("Request created").build();
            }
        }

        return RequestResponse.builder().message("Invalid request").build();
    }

    public List<FollowUpRequest> getFollowUpRequest() {
        return followUpRequestRepository.findAll();
    }


    public List<Object[]> getFollowUpRequestsByStatusName(String statusName) {
        return followUpRequestRepository.findByStatusName(statusName);
    }

}
