package com.example.Inmopro.v1;

import com.example.Inmopro.v1.Controller.Request.RequestResponse;
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
import com.example.Inmopro.v1.Service.Request.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private RequestService requestService;

    @Mock
    private HttpServletRequest httpRequest;

    @Test
    void testCreate_Success() throws IOException, MessagingException {
        // Configura el objeto de solicitud
        RequestRequest requestRequest = new RequestRequest();
        requestRequest.setRequestType(1);
        requestRequest.setDescription("Test description");

        // Configura el comportamiento del mock
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer test-token");
        when(jwtService.getUsernameFromToken("test-token")).thenReturn("test@example.com");

        // Configura el usuario simulado
        Users mockUser = new Users();
        Roles mockRole = new Roles();
        mockRole.setId(1);
        mockUser.setRole(mockRole);
        mockUser.setEmail("test@example.com");

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        RequestType mockRequestType = new RequestType();
        mockRequestType.setId(1);
        when(requestTypeRepository.findById(1)).thenReturn(Optional.of(mockRequestType));

        RequestStatus mockRequestStatus = new RequestStatus();
        mockRequestStatus.setId(1);
        when(requestStatusRepository.findById(1)).thenReturn(Optional.of(mockRequestStatus));

        // Mock del Resource y su InputStream
        Resource mockResource = mock(Resource.class);
        InputStream inputStream = new ByteArrayInputStream("Test email content".getBytes());
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource("classpath:static/RequestMessage.html")).thenReturn(mockResource);

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

    @Test
    void testCreate_InvalidAuthorization() throws IOException, MessagingException {
        RequestRequest requestRequest = new RequestRequest();
        requestRequest.setRequestType(1);
        requestRequest.setDescription("Test description");

        when(httpRequest.getHeader("Authorization")).thenReturn(null);

        RequestResponse response = requestService.create(requestRequest, httpRequest);

        assertNotNull(response);
        assertEquals("Invalid request", response.getMessage());

        verify(requestRepository, never()).save(any(Request.class));
        verify(followUpRequestRepository, never()).save(any(FollowUpRequest.class));
        verify(mailService, never()).sendHtmlEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testGetFollowUpRequest_Success() {
        List<FollowUpRequest> mockFollowUpRequests = List.of(new FollowUpRequest());

        when(followUpRequestRepository.findAll()).thenReturn(mockFollowUpRequests);

        List<FollowUpRequest> result = requestService.getFollowUpRequest();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(followUpRequestRepository).findAll();
    }

    @Test
    void testGetFollowUpRequestsByStatusName_Success() {
    }

    @Test
    void testCancel_Success() {
        Integer requestCancelId = 1;

        Request mockRequest = new Request();
        mockRequest.setRequestId(requestCancelId);
        mockRequest.setCreatedAt(LocalDateTime.now().minusHours(1));

        RequestStatus mockRequestStatusInitial = new RequestStatus();
        mockRequestStatusInitial.setId(1);
        mockRequest.setStatusId(mockRequestStatusInitial);

        RequestStatus mockRequestStatus = new RequestStatus();
        mockRequestStatus.setId(4);

        // Configura el comportamiento del mock
        when(requestRepository.findByRequestId(requestCancelId)).thenReturn(Optional.of(mockRequest));
        when(requestStatusRepository.findById(4)).thenReturn(Optional.of(mockRequestStatus));

        // Llama al método que estás probando
        RequestResponse response = requestService.cancel(requestCancelId);

        // Afirmaciones para verificar el comportamiento esperado
        assertNotNull(response);
        assertEquals("Request canceled", response.getMessage());

        // Verifica que los métodos en los repositorios se llamaron como se esperaba
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void testCancel_InvalidRequest() {
        Integer requestCancelId = 1;

        when(requestRepository.findByRequestId(requestCancelId)).thenReturn(Optional.empty());

        RequestResponse response = requestService.cancel(requestCancelId);

        assertNotNull(response);
        assertEquals("Invalid request", response.getMessage());

        verify(requestRepository, never()).save(any(Request.class));
    }
}
