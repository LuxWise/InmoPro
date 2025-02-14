package com.example.Inmopro.v1.Service.Monitor;

import com.example.Inmopro.v1.Controller.MonitorCon.MonitorResponse;
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

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
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
                        return RequestResponse.builder().message("User have a pending request").build();
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
                    try {
                        Resource resource = resourceLoader.getResource("classpath:static/RequestMessage.html");
                        if (!resource.exists()) {
                            throw new RuntimeException("File not found");
                        }
                        try (InputStream inputStream = resource.getInputStream()) {
                            String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                            mailService.sendHtmlEmail(emailRequest, "Request created", htmlContent);
                        } catch (IOException e) {
                            return RequestResponse.builder().message("Error loading email template").build();
                        }
                    } catch (RuntimeException e) {
                        return RequestResponse.builder().message("Error loading email template").build();
                    }


                    return RequestResponse.builder().message("Request created").build();


                }else {
                    return RequestResponse.builder().message("User invalid by rol").build();
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

    public MonitorResponse getAllRequestsByRol(HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);

            Optional<Users> userOptionalToken = usersRepository.findByEmail(email);
            Integer monitorId = userOptionalToken.get().getUser_id();
            //Integer monitorId = isUserMonitor(httpRequest);

            Optional<Object[]> request = requestRepository.findAllRequestsByRol(monitorId);
            if (request.isEmpty()) {
                return MonitorResponse.builder()
                        .message("No se encontró la solicitud con los parámetros proporcionados.")
                        .build();
            }

            return MonitorResponse.builder()
                    .message("Solicitud encontrada.")
                    .data(request.get())
                    .build();
        }
        return MonitorResponse.builder().message("Invalid request: missing authorization token").build();
    }


    public MonitorResponse getRequestById(Integer requestId, HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);

            Optional<Users> userOptionalToken = usersRepository.findByEmail(email);
            Integer monitorId = userOptionalToken.get().getUser_id();
            //Integer monitorId = isUserMonitor(httpRequest);

            Optional<Object[]> request = requestRepository.findByIdAndMonitorId(requestId, monitorId);

            if (request.isEmpty()) {
                return MonitorResponse.builder()
                        .message("No se encontró la solicitud con los parámetros proporcionados.")
                        .build();
            }

            return MonitorResponse.builder()
                    .message("Solicitud encontrada.")
                    .data(request.get())
                    .build();
        }
        return MonitorResponse.builder().message("Invalid request: missing authorization token").build();
    }


    public MonitorResponse getAllRequestsByRolAndPending(Integer statusRequestId, HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String email = jwtService.getUsernameFromToken(token);

            Optional<Users> userOptionalToken = usersRepository.findByEmail(email);
            Integer monitorId = userOptionalToken.get().getUser_id();
            //Integer monitorId = isUserMonitor(httpRequest);
            Optional<Object[]> request = requestRepository.findAllRequestsByRolAndPending(monitorId, statusRequestId);

            if (request.isEmpty()) {
                return MonitorResponse.builder()
                        .message("No se encontró la solicitud con los parámetros proporcionados.")
                        .build();
            }

            return MonitorResponse.builder()
                    .message("Solicitud encontrada.")
                    .data(request.get())
                    .build();
        }
        return MonitorResponse.builder().message("Invalid request: by ").build();
    }
    public RequestResponse process(Integer requestId, HttpServletRequest httpRequest) throws MessagingException, IOException {
        return processRequestWithNewStatus(requestId, httpRequest, 2, "Request Process", "RequestProcessedMessage.html");

    }

    public RequestResponse cancelRequest(Integer requestId, HttpServletRequest httpRequest) throws MessagingException, IOException {
        return processRequestWithNewStatus(requestId, httpRequest, 4, "Request Cancelled", "RequestCancelMessage.html");

    }

    public RequestResponse onHoldRequest(Integer requestId, HttpServletRequest httpRequest) throws MessagingException, IOException {
        return processRequestWithNewStatus(requestId, httpRequest, 6, "Request On Hold", "RequestMessage.html");

    }
    private RequestResponse processRequestWithNewStatus(Integer requestId, HttpServletRequest httpRequest, int newStatusId, String successMessage, String emailTemplate) throws MessagingException, IOException {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return RequestResponse.builder().message("Invalid or missing Authorization header").build();
        }

        String token = authorizationHeader.substring(7);
        String email;
        try {
            email = jwtService.getUsernameFromToken(token);
        } catch (Exception e) {
            return RequestResponse.builder().message("Invalid token").build();
        }

        Optional<Users> userOptionalToken = usersRepository.findByEmail(email);
        if (userOptionalToken.isEmpty() || userOptionalToken.get().getRole().getId() != 3) {
            return RequestResponse.builder().message("Invalid process by role").build();
        }

        Optional<Request> requestOptional = requestRepository.findByRequestId(requestId);
        if (requestOptional.isEmpty()) {
            return RequestResponse.builder().message("Request not found").build();
        }

        Request request = requestOptional.get();

        // Verificar si el estado actual es "Cancelado"
        if (request.getStatusId().getId() == 4) { // Suponiendo que 4 es el ID del estado "Cancelado"
            return RequestResponse.builder().message("Cannot change the status of a cancelled request").build();
        }

        // Verificar si la solicitud ya está en el nuevo estado
        if (request.getStatusId().getId() == newStatusId) {
            return RequestResponse.builder().message("Request is already in the desired status").build();
        }

        // Verificar si la solicitud es diferente a un status que no puede ser modificado
        if (request.getStatusId().getId() == 3 ) {
            return RequestResponse.builder().message("Request is already approve").build();
        }
        if(request.getStatusId().getId() == 4){
            return RequestResponse.builder().message("Request is already cancelled").build();
        }
        if(request.getStatusId().getId() == 5){
            return RequestResponse.builder().message("Request is already complete").build();
        }


        Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(newStatusId);
        if (requestStatusOptional.isEmpty()) {
            return RequestResponse.builder().message("Request status not found").build();
        }

        RequestStatus newStatus = requestStatusOptional.get();
        request.setStatusId(newStatus);
        requestRepository.save(request);

        Resource resource = resourceLoader.getResource("classpath:static/" + emailTemplate);
        try (InputStream inputStream = resource.getInputStream()) {
            String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            mailService.sendHtmlEmail(userOptionalToken.get().getEmail(), successMessage, htmlContent);
        } catch (IOException e) {
            return RequestResponse.builder().message("Error sending email").build();
        }

        return RequestResponse.builder().message(successMessage).build();
    }

    /*private RequestResponse processRequestWithNewStatus(Integer requestId, HttpServletRequest httpRequest, int newStatusId, String successMessage, String emailTemplate) throws MessagingException, IOException {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return RequestResponse.builder().message("Invalid or missing Authorization header").build();
        }

        String token = authorizationHeader.substring(7);
        String email;
        try {
            email = jwtService.getUsernameFromToken(token);
        } catch (Exception e) {
            return RequestResponse.builder().message("Invalid token").build();
        }

        Optional<Users> userOptionalToken = usersRepository.findByEmail(email);
        if (userOptionalToken.isEmpty() || userOptionalToken.get().getUser_id() != 3) {
            return RequestResponse.builder().message("Invalid process by role").build();
        }

        Optional<Request> requestOptional = requestRepository.findByRequestId(requestId);
        if (requestOptional.isEmpty() || requestOptional.get().getStatusId().getId() != 2) { // Solo se pueden modificar solicitudes en proceso
            return RequestResponse.builder().message("Invalid process, request not in process").build();
        }

        Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(newStatusId);
        if (requestStatusOptional.isEmpty()) {
            return RequestResponse.builder().message("Request status not found").build();
        }

        Request request = requestOptional.get();
        RequestStatus newStatus = requestStatusOptional.get();

        request.setStatusId(newStatus);
        requestRepository.save(request);

        Resource resource = resourceLoader.getResource("classpath:static/" + emailTemplate);
        try (InputStream inputStream = resource.getInputStream()) {
            String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            mailService.sendHtmlEmail(userOptionalToken.get().getEmail(), successMessage, htmlContent);
        } catch (IOException e) {
            return RequestResponse.builder().message("Error sending email").build();
        }

        return RequestResponse.builder().message(successMessage).build();
    }
*/
}