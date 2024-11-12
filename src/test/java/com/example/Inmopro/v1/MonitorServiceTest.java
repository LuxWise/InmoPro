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

        //when(requestRepository.existsByTenantAndStatusId(mockUserTenant, mockRequestStatus)).thenReturn(true);

        // Mock del Resource y su InputStream
        Resource mockResource = mock(Resource.class);
        InputStream inputStream = new ByteArrayInputStream("Test email content".getBytes());
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource("classpath:static/RequestMessage.html")).thenReturn(mockResource);

        RequestResponse response = monitorService.create(requestMonitor, httpRequest);

        assertNotNull(response);
        assertEquals("Request created", response.getMessage());

        verify(requestRepository).save(any(Request.class));
        verify(followUpRequestRepository).save(any(FollowUpRequest.class));
        verify(mailService).sendHtmlEmail(eq("tenant@example.com"), eq("Request created"), anyString());

        verify(usersRepository).findByEmail("monitor@example.com");
        verify(usersRepository).findByEmail("tenant@example.com");
        verify(requestTypeRepository).findById(1);
        verify(requestStatusRepository).findById(2);
    }
}
