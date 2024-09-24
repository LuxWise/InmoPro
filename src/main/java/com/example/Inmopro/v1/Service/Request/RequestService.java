package com.example.Inmopro.v1.Service.Request;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Model.Request.Request;
import com.example.Inmopro.v1.Model.Request.RequestType;
import com.example.Inmopro.v1.Model.Users.Users;
import com.example.Inmopro.v1.Repository.FollowUpRequestRepository;
import com.example.Inmopro.v1.Repository.RequestRepository;
import com.example.Inmopro.v1.Repository.RequestTypeRepository;
import com.example.Inmopro.v1.Repository.UsersRepository;
import com.example.Inmopro.v1.Service.Jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final FollowUpRequestRepository followUpRequestRepository;
    private final RequestRepository requestRepository;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final RequestTypeRepository requestTypeRepository;

    public RequestResponse create(RequestRequest request, HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);
            Optional<Users> userOptional = usersRepository.findByEmail(email);
            Optional<RequestType> requestTypeOptional = requestTypeRepository.findById(request.getRequestType());

            if (userOptional.isPresent() && requestTypeOptional.isPresent()) {
                Users users = userOptional.get();
                Integer userRole = users.getRole().getId();
                RequestType requestType = requestTypeOptional.get();

                if (userRole != 1) {
                    return RequestResponse.builder().message("User invalid").build();
                }

                // Crear la entidad Request con la relación a la entidad RequestType
                Request requestEntity = Request.builder()
                        .tenant(users)  // Aquí pasamos la entidad Users
                        .requestTypeId(requestType)  // Pasamos la entidad RequestType
                        .status_id(1)
                        .description(request.getDescription())
                        .build();

                requestRepository.save(requestEntity);

                System.out.println("Request ID: " + requestEntity.getRequestId());

                FollowUpRequest followUpRequest = FollowUpRequest.builder()
                        .requestId(requestEntity.getRequestId())
                        .statusId(1)
                        .inForce(true)
                        .build();

                followUpRequestRepository.save(followUpRequest);

                return RequestResponse.builder().message("Request created").build();
            }
        }

        return RequestResponse.builder().message("Invalid request").build();
    }
}

