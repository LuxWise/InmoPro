package com.example.Inmopro.v1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.Inmopro.v1.Controller.MonitorCon.Response;
import com.example.Inmopro.v1.Controller.Request.RequestResponse;
import com.example.Inmopro.v1.Dto.Request.RequestMonitor;
import com.example.Inmopro.v1.Dto.Request.RequestRequest;
import com.example.Inmopro.v1.Exception.UnauthorizedAccessException;
import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Model.Request.Request;
import com.example.Inmopro.v1.Model.Request.RequestStatus;
import com.example.Inmopro.v1.Model.Request.RequestType;
import com.example.Inmopro.v1.Model.Users.Roles;
import com.example.Inmopro.v1.Model.Users.Users;
import com.example.Inmopro.v1.Repository.*;
import com.example.Inmopro.v1.Service.Jwt.JwtService;
import com.example.Inmopro.v1.Service.Mail.MailService;
import com.example.Inmopro.v1.Service.Monitor.MonitorService;
import com.example.Inmopro.v1.Service.Request.RequestService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.servlet.http.HttpServletRequest;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.management.relation.Role;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MonitorServiceTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private RequestTypeRepository requestTypeRepository;
    @Mock
    private RequestStatusRepository requestStatusRepository;
    @Mock
    private FollowUpRequestRepository followUpRequestRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private MailService mailService;
    @Mock
    private ResourceLoader resourceLoader;
    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private MonitorService monitorService;

    // Creacion
    @Test
    void testCreateRequestMonitor_Success() throws IOException, MessagingException {
        RequestMonitor requestMonitor = new RequestMonitor();
        requestMonitor.setRequestType(1);
        requestMonitor.setDescription("Test description");
        requestMonitor.setEmail("tenant@example.com");

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer test-token");
        when(jwtService.getUsernameFromToken("test-token")).thenReturn("monitor@example.com");

        Users mockUserSession = new Users();
        Roles monitorRole = new Roles();
        monitorRole.setId(3);  // id para el rol de monitor
        mockUserSession.setRole(monitorRole);
        mockUserSession.setEmail("monitor@example.com");

        when(usersRepository.findByEmail("monitor@example.com")).thenReturn(Optional.of(mockUserSession));

        Users mockUserTenant = new Users();
        Roles tenantRole = new Roles();
        tenantRole.setId(1);  // id para el rol de tenant
        mockUserTenant.setRole(tenantRole);
        mockUserTenant.setEmail("tenant@example.com");

        when(usersRepository.findByEmail("tenant@example.com")).thenReturn(Optional.of(mockUserTenant));

        RequestType mockRequestType = new RequestType();
        mockRequestType.setId(1);
        when(requestTypeRepository.findById(1)).thenReturn(Optional.of(mockRequestType));

        RequestStatus mockRequestStatus = new RequestStatus();
        mockRequestStatus.setId(2);  // Estado en progreso
        when(requestStatusRepository.findById(2)).thenReturn(Optional.of(mockRequestStatus));

        when(requestRepository.existsByTenantAndStatusId(mockUserTenant, mockRequestStatus)).thenReturn(false);

        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:static/RequestMessage.html")).thenReturn(mockResource);
        when(mockResource.exists()).thenReturn(true);  // El recurso existe
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream("<html>Success</html>".getBytes())); // Simulamos que podemos leerlo correctamente

        // Simulando que se envía el correo
        doNothing().when(mailService).sendHtmlEmail(anyString(), anyString(), anyString());

        RequestResponse response = monitorService.create(requestMonitor, httpRequest);

        assertNotNull(response);
        assertEquals("Request created", response.getMessage());  // Mensaje esperado cuando la solicitud se crea correctamente

        verify(requestRepository).save(any(Request.class));
        verify(followUpRequestRepository).save(any(FollowUpRequest.class));
        verify(mailService).sendHtmlEmail(eq("tenant@example.com"), eq("Request created"), anyString());

        verify(usersRepository).findByEmail("monitor@example.com");
        verify(usersRepository).findByEmail("tenant@example.com");
        verify(requestTypeRepository).findById(1);
        verify(requestStatusRepository).findById(2);
    }

    @Test
    void testCreateRequestMonitor_InvalidRole() throws IOException, MessagingException {
        RequestMonitor requestMonitor = new RequestMonitor();
        requestMonitor.setRequestType(1);
        requestMonitor.setDescription("Test description");
        requestMonitor.setEmail("nonOwnerOrTenant@example.com");

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer test-token");
        when(jwtService.getUsernameFromToken("test-token")).thenReturn("monitor@example.com");

        Users mockUserSession = new Users();
        Roles monitorRole = new Roles();
        monitorRole.setId(3);  // Monitor role
        mockUserSession.setRole(monitorRole);
        mockUserSession.setEmail("monitor@example.com");

        when(usersRepository.findByEmail("monitor@example.com")).thenReturn(Optional.of(mockUserSession));

        Users mockUserInvalidRole = new Users();
        Roles invalidRole = new Roles();
        invalidRole.setId(4);  // Invalid role
        mockUserInvalidRole.setRole(invalidRole);
        mockUserInvalidRole.setEmail("nonOwnerOrTenant@example.com");

        when(usersRepository.findByEmail("nonOwnerOrTenant@example.com")).thenReturn(Optional.of(mockUserInvalidRole));


        RequestType mockRequestType = new RequestType();
        mockRequestType.setId(1);
        when(requestTypeRepository.findById(1)).thenReturn(Optional.of(mockRequestType));

        RequestStatus mockRequestStatus = new RequestStatus();
        mockRequestStatus.setId(2);  // Estado en progreso
        when(requestStatusRepository.findById(2)).thenReturn(Optional.of(mockRequestStatus));


        Resource mockResource = mock(Resource.class);
        InputStream inputStream = new ByteArrayInputStream("Test email content".getBytes());

        RequestResponse response = monitorService.create(requestMonitor, httpRequest);

        assertEquals("User invalid by rol", response.getMessage());
    }

    @Test
    void testCreateRequestMonitor_UnauthenticatedUser() throws MessagingException, IOException {
        RequestMonitor requestMonitor = new RequestMonitor();

        when(httpRequest.getHeader("Authorization")).thenReturn(null);

        RequestResponse response = monitorService.create(requestMonitor, httpRequest);

        assertEquals("Invalid request: missing authorization token", response.getMessage());
    }

    @Test
    void testCreateRequestMonitor_PendingRequest() throws IOException, MessagingException {

        RequestMonitor requestMonitor = new RequestMonitor();
        requestMonitor.setRequestType(1);
        requestMonitor.setDescription("Test description");
        requestMonitor.setEmail("tenant@example.com");

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer test-token");
        when(jwtService.getUsernameFromToken("test-token")).thenReturn("monitor@example.com");

        Users mockUserSession = new Users();
        Roles monitorRole = new Roles();
        monitorRole.setId(3);  // id para el rol de monitor
        mockUserSession.setRole(monitorRole);
        mockUserSession.setEmail("monitor@example.com");

        when(usersRepository.findByEmail("monitor@example.com")).thenReturn(Optional.of(mockUserSession));

        Users mockUserTenant = new Users();
        Roles tenantRole = new Roles();
        tenantRole.setId(1);  // id para el rol de tenant
        mockUserTenant.setRole(tenantRole);
        mockUserTenant.setEmail("tenant@example.com");

        when(usersRepository.findByEmail("tenant@example.com")).thenReturn(Optional.of(mockUserTenant));

        RequestType mockRequestType = new RequestType();
        mockRequestType.setId(1);
        when(requestTypeRepository.findById(1)).thenReturn(Optional.of(mockRequestType));

        RequestStatus mockRequestStatus = new RequestStatus();
        mockRequestStatus.setId(2);  // Estado en progreso
        when(requestStatusRepository.findById(2)).thenReturn(Optional.of(mockRequestStatus));

        when(requestRepository.existsByTenantAndStatusId(mockUserTenant, mockRequestStatus)).thenReturn(true);

        RequestResponse response = monitorService.create(requestMonitor, httpRequest);

        assertEquals("User have a pending request", response.getMessage());
    }
    @Test
    void testCreateRequestMonitor_EmailTemplateError() throws IOException, MessagingException {
        RequestMonitor requestMonitor = new RequestMonitor();
        requestMonitor.setRequestType(1);
        requestMonitor.setDescription("Test description");
        requestMonitor.setEmail("tenant@example.com");

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer test-token");
        when(jwtService.getUsernameFromToken("test-token")).thenReturn("monitor@example.com");

        Users mockUserSession = new Users();
        Roles monitorRole = new Roles();
        monitorRole.setId(3);  // id para el rol de monitor
        mockUserSession.setRole(monitorRole);
        mockUserSession.setEmail("monitor@example.com");

        when(usersRepository.findByEmail("monitor@example.com")).thenReturn(Optional.of(mockUserSession));

        Users mockUserTenant = new Users();
        Roles tenantRole = new Roles();
        tenantRole.setId(1);  // id para el rol de tenant
        mockUserTenant.setRole(tenantRole);
        mockUserTenant.setEmail("tenant@example.com");

        when(usersRepository.findByEmail("tenant@example.com")).thenReturn(Optional.of(mockUserTenant));

        RequestType mockRequestType = new RequestType();
        mockRequestType.setId(1);
        when(requestTypeRepository.findById(1)).thenReturn(Optional.of(mockRequestType));

        RequestStatus mockRequestStatus = new RequestStatus();
        mockRequestStatus.setId(2);  // Estado en progreso
        when(requestStatusRepository.findById(2)).thenReturn(Optional.of(mockRequestStatus));

        when(requestRepository.existsByTenantAndStatusId(mockUserTenant, mockRequestStatus)).thenReturn(false);

        doThrow(new RuntimeException("File not found")).when(resourceLoader).getResource("classpath:static/RequestMessage.html");


        RequestResponse response = monitorService.create(requestMonitor, httpRequest);

        assertNotNull(response);
        assertEquals("Error loading email template", response.getMessage());

    }

    //consultas_propias del rol
    @Test
    void testGetAllRequestsByRol_Success() {
        String token = "test-token";
        String email = "monitor@example.com";

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn(email);

        Users mockUser = new Users();
        mockUser.setUser_id(1);
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Object[] mockRequest = new Object[]{};
        when(requestRepository.findAllRequestsByRol(1)).thenReturn(Optional.of(mockRequest));

        Response response = monitorService.getAllRequestsByRol(httpRequest);

        assertNotNull(response);
        assertEquals("Solicitud encontrada.", response.getMessage());
        assertEquals(mockRequest, response.getData());

        verify(jwtService).getUsernameFromToken(token);
        verify(usersRepository).findByEmail(email);
        verify(requestRepository).findAllRequestsByRol(1);
    }

    @Test
    void testGetAllRequestsByRol_NoRequestsFound() {
        String token = "test-token";
        String email = "monitor@example.com";

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn(email);

        Users mockUser = new Users();
        mockUser.setUser_id(1);
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        when(requestRepository.findAllRequestsByRol(1)).thenReturn(Optional.empty());

        Response response = monitorService.getAllRequestsByRol(httpRequest);

        assertNotNull(response);
        assertEquals("No se encontró la solicitud con los parámetros proporcionados.", response.getMessage());
        assertNull(response.getData());

        verify(jwtService).getUsernameFromToken(token);
        verify(usersRepository).findByEmail(email);
        verify(requestRepository).findAllRequestsByRol(1);
    }

    @Test
    void testGetAllRequestsByRol_InvalidToken() {
        when(httpRequest.getHeader("Authorization")).thenReturn(null);

        Response response = monitorService.getAllRequestsByRol(httpRequest);

        assertNotNull(response);
        assertEquals("Invalid request: missing authorization token", response.getMessage());
        assertNull(response.getData());

        verifyNoInteractions(jwtService, usersRepository, requestRepository);
    }

    //consultas propias del rol por solicitud

    @Test
    void testGetRequestById_Success() {
        String token = "test-token";
        String email = "monitor@example.com";
        Integer requestId = 1;

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn(email);

        Users mockUser = new Users();
        mockUser.setUser_id(1);
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Object[] mockRequest = new Object[]{/* Datos simulados */};
        when(requestRepository.findByIdAndMonitorId(requestId, 1)).thenReturn(Optional.of(mockRequest));

        Response response = monitorService.getRequestById(requestId, httpRequest);

        assertNotNull(response);
        assertEquals("Solicitud encontrada.", response.getMessage());
        assertEquals(mockRequest, response.getData());

        verify(jwtService).getUsernameFromToken(token);
        verify(usersRepository).findByEmail(email);
        verify(requestRepository).findByIdAndMonitorId(requestId, 1);
    }

    @Test
    void testGetRequestById_NoRequestFound() {
        String token = "test-token";
        String email = "monitor@example.com";
        Integer requestId = 1;

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn(email);

        Users mockUser = new Users();
        mockUser.setUser_id(1);
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        when(requestRepository.findByIdAndMonitorId(requestId, 1)).thenReturn(Optional.empty());

        Response response = monitorService.getRequestById(requestId, httpRequest);

        assertNotNull(response);
        assertEquals("No se encontró la solicitud con los parámetros proporcionados.", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testGetRequestById_InvalidToken() {
        when(httpRequest.getHeader("Authorization")).thenReturn(null);

        Response response = monitorService.getRequestById(1, httpRequest);

        assertNotNull(response);
        assertEquals("Invalid request: missing authorization token", response.getMessage());
        assertNull(response.getData());

        verifyNoInteractions(jwtService, usersRepository, requestRepository);
    }
    @Test
    void testGetAllRequestsByRolAndPending_Success() {
        String token = "test-token";
        String email = "monitor@example.com";
        Integer statusRequestId = 2;

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn(email);

        Users mockUser = new Users();
        mockUser.setUser_id(1);
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Object[] mockRequest = new Object[]{/* Datos simulados */};
        when(requestRepository.findAllRequestsByRolAndPending(1, statusRequestId)).thenReturn(Optional.of(mockRequest));

        Response response = monitorService.getAllRequestsByRolAndPending(statusRequestId, httpRequest);

        assertNotNull(response);
        assertEquals("Solicitud encontrada.", response.getMessage());
        assertEquals(mockRequest, response.getData());

        verify(jwtService).getUsernameFromToken(token);
        verify(usersRepository).findByEmail(email);
        verify(requestRepository).findAllRequestsByRolAndPending(1, statusRequestId);
    }

    @Test
    void testGetAllRequestsByRolAndPending_NoRequestsFound() {
        String token = "test-token";
        String email = "monitor@example.com";
        Integer statusRequestId = 2;

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn(email);

        Users mockUser = new Users();
        mockUser.setUser_id(1);
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        when(requestRepository.findAllRequestsByRolAndPending(1, statusRequestId)).thenReturn(Optional.empty());

        Response response = monitorService.getAllRequestsByRolAndPending(statusRequestId, httpRequest);

        assertNotNull(response);
        assertEquals("No se encontró la solicitud con los parámetros proporcionados.", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testGetAllRequestsByRolAndPending_InvalidToken() {
        when(httpRequest.getHeader("Authorization")).thenReturn(null);

        Response response = monitorService.getAllRequestsByRolAndPending(2, httpRequest);

        assertNotNull(response);
        assertEquals("Invalid request: missing authorization token", response.getMessage());
        assertNull(response.getData());

        verifyNoInteractions(jwtService, usersRepository, requestRepository);
    }


}