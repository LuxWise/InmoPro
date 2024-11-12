package com.example.Inmopro.v1.Service.Monitor;

import com.example.Inmopro.v1.Controller.MonitorCon.Response;
import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Dto.Request.RequestMonitor;
import com.example.Inmopro.v1.Exception.UnauthorizedAccessException;
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

    private final RequestRepository requestRepository;
    private final ResourceLoader resourceLoader;
    private final RequestTypeRepository requestTypeRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final FollowUpRequestRepository followUpRequestRepository;
    private final MailService mailService;

    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    public RequestResponse create(RequestMonitor request, HttpServletRequest httpRequest) throws IOException, MessagingException {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);

            Optional<Users> userOptionalToken = usersRepository.findByEmail(email);
            Optional<RequestType> requestTypeOptional = requestTypeRepository.findById(request.getRequestType());
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(2);

            Optional<Users> userOptionalClient = usersRepository.findByEmail(request.getEmail());


            if(userOptionalToken.isPresent() && requestTypeOptional.isPresent() && requestStatusOptional.isPresent() && userOptionalClient.isPresent()) {
                Users userRequest = userOptionalClient.get();// owner or tenant
                Integer rolUser = userRequest.getRole().getId();
                String emailRequest = userOptionalClient.get().getEmail();

                //Integer userSessionRequestMonitor = userOptionalToken.get().getRole().getId();


                if (rolUser == 1 || rolUser == 2) { //validar la sesion del usuario o implentar esto en este userSessionRequestMonitor == 3
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
                    } catch (IOException e) {
                        return RequestResponse.builder().message("Error loading email template").build();
                    }

                    return RequestResponse.builder().message("Request created").build();


                }else {
                    return RequestResponse.builder().message("User invalid").build();
                }

            }

        }

        return RequestResponse.builder().message("Invalid request: missing authorization token").build();
    }


    private Integer isUserMonitor(HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);
            Optional<Users> userOptional = usersRepository.findByEmail(email);

            // Si el rol no es 'Monitor', lanzamos una excepción
            return userOptional.map(user -> {
                if (user.getRole().getId() != 3) {
                    throw new UnauthorizedAccessException("Acceso denegado por rol");
                }
                return user.getUser_id(); // Si el usuario tiene el rol adecuado, devolvemos true
            }).orElseThrow(() -> new UnauthorizedAccessException("Usuario no encontrado"));
        }
        throw new UnauthorizedAccessException("Acceso denegado por rol");
    }

    private void checkIfUserIsMonitor(HttpServletRequest httpRequest) {
        Integer userId = isUserMonitor(httpRequest);
    }

    public Response getAllRequestsByRol(HttpServletRequest httpRequest) {
        Integer monitorId = isUserMonitor(httpRequest);

        Optional<Object[]> request = requestRepository.findAllRequestsByRol(monitorId);
        if (request.isEmpty()) {
            return Response.builder()
                    .message("No se encontró la solicitud con los parámetros proporcionados.")
                    .build();
        }

        return Response.builder()
                .message("Solicitud encontrada.")
                .data(request.get())
                .build();
    }


    public Response getRequestById(Integer requestId, HttpServletRequest httpRequest) {
        Integer monitorId = isUserMonitor(httpRequest);

        Optional<Object[]> request = requestRepository.findByIdAndMonitorId(requestId, monitorId);

        if (request.isEmpty()) {
            return Response.builder()
                    .message("No se encontró la solicitud con los parámetros proporcionados.")
                    .build();
        }

        return Response.builder()
                .message("Solicitud encontrada.")
                .data(request.get())
                .build();
    }


    public Response getAllRequestsByRolAndPending(Integer statusRequestId, HttpServletRequest httpRequest) {
        Integer monitorId = isUserMonitor(httpRequest);
        Optional<Object[]> request = requestRepository.findAllRequestsByRolAndPending(monitorId, statusRequestId);

        if (request.isEmpty()) {
            return Response.builder()
                    .message("No se encontró la solicitud con los parámetros proporcionados.")
                    .build();
        }

        return Response.builder()
                .message("Solicitud encontrada.")
                .data(request.get())
                .build();
    }
}
