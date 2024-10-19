package com.example.Inmopro.v1;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Model.Calculator;
import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Model.Request.Request;
import com.example.Inmopro.v1.Model.Request.RequestStatus;
import com.example.Inmopro.v1.Model.Request.RequestType;
import com.example.Inmopro.v1.Model.Users.Roles;
import com.example.Inmopro.v1.Model.Users.Users;
import com.example.Inmopro.v1.Repository.*;
import com.example.Inmopro.v1.Service.Jwt.JwtService;
import com.example.Inmopro.v1.Service.Mail.MailService;
import com.example.Inmopro.v1.Service.Request.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


import java.io.IOException;
import java.util.Optional;

import jakarta.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RequestServiceTest {

    @Mock
    private FollowUpRequestRepository followUpRequestRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RequestTypeRepository requestTypeRepository;

    @Mock
    private RequestStatusRepository requestStatusRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private RequestService requestService;

    @Mock
    private HttpServletRequest httpRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Test
void testCreate_Success() throws IOException, MessagingException {
    // Configura el objeto de solicitud
    RequestRequest requestRequest = new RequestRequest();
    requestRequest.setRequestType(1); // Ajusta según tus necesidades
    requestRequest.setDescription("Test description");

    // Configura el usuario simulado
    Users mockUser = new Users();
    Roles mockRole = new Roles();
    mockRole.setId(1); // Asegúrate de que el rol sea válido
    mockUser.setRole(mockRole);
    mockUser.setEmail("test@example.com");

    // Configura el comportamiento del mock
    when(httpRequest.getHeader("Authorization")).thenReturn("Bearer test-token");
    when(jwtService.getUsernameFromToken("test-token")).thenReturn("test@example.com");
    when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

    RequestType mockRequestType = new RequestType();
    mockRequestType.setId(1); // Asegúrate de que esto coincide con tu lógica
    when(requestTypeRepository.findById(1)).thenReturn(Optional.of(mockRequestType));

    RequestStatus mockRequestStatus = new RequestStatus();
    mockRequestStatus.setId(1); // Ajusta según tu lógica
    when(requestStatusRepository.findById(1)).thenReturn(Optional.of(mockRequestStatus));

    // Llama al método que estás probando
    RequestResponse response = requestService.create(requestRequest, httpRequest);

    // Afirmaciones para verificar el comportamiento esperado
    assertNotNull(response);
    assertEquals("Request created", response.getMessage());

    // Verifica que los métodos en los repositorios se llamaron como se esperaba
    verify(requestRepository).save(any(Request.class));
    verify(followUpRequestRepository).save(any(FollowUpRequest.class));
    verify(mailService).sendHtmlEmail(eq("test@example.com"), eq("Request created"), anyString());
}


}
