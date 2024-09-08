package com.example.Inmopro.v1.Service.Request;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Model.Request.Request;
import com.example.Inmopro.v1.Model.Users.Users;
import com.example.Inmopro.v1.Repository.RequestRepository;
import com.example.Inmopro.v1.Repository.UsersRepository;
import com.example.Inmopro.v1.Service.Jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    public RequestResponse create(RequestRequest request, HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);
            Optional<Users> UserOptional = usersRepository.findByEmail(email);

            if (UserOptional.isPresent()) {
                Users users = UserOptional.get();
                Integer userId = users.getUser_id();
                Integer userRole = users.getRole_id();

                if (userRole != 1){
                    return RequestResponse.builder().message("User invalid").build();
                }

                Request requestEntity = Request.builder()
                        .tenant_id(userId)
                        .request_type_id(request.getRequestType())
                        .status_id(1)
                        .description(request.getDescription())
                        .build();

                requestRepository.save(requestEntity);


            }
        }

        return RequestResponse.builder().message("Request create").build();

    }

}
