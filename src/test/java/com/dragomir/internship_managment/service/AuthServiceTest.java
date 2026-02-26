package com.dragomir.internship_managment.service;

import com.dragomir.internship_managment.domain.Student;
import com.dragomir.internship_managment.domain.User;
import com.dragomir.internship_managment.domain.UserRole;
import com.dragomir.internship_managment.dto.AuthResponseDTO;
import com.dragomir.internship_managment.dto.LoginDTO;
import com.dragomir.internship_managment.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private  AuthenticationManager authenticationManager;

    @Mock
    private  JwtUtil jwtUtil;

    @Mock
    private  UserService userService;

    @InjectMocks
    private  AuthService authService;

    @Test
    void shouldAuthenticateUserAndReturnAuthenticationDTO() {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setEmail("test@email.com");
            loginDTO.setPassword("testpassword");

            Authentication auth = mock(Authentication.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

            when(jwtUtil.generateToken(auth)).thenReturn("new-jwt-token");

            User user = new Student();
            user.setId(1L);
            user.setEmail("test@email.com");
            user.setRole(UserRole.STUDENT);

            when(userService.findByEmail(loginDTO.getEmail())).thenReturn(user);


        AuthResponseDTO ardto = authService.authenticateUser(loginDTO);

        assertNotNull(ardto);
        assertEquals("new-jwt-token", ardto.getToken());
        assertEquals(1L, ardto.getId());
        assertEquals("test@email.com",  ardto.getEmail());
        assertEquals("STUDENT", ardto.getRole());

        verify(authenticationManager).authenticate(any());
        verify(jwtUtil).generateToken(auth);
        verify(userService).findByEmail("test@email.com");
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenUserIsNotValid() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@email.com");
        loginDTO.setPassword("testpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("E-mail i/ili lozinka nisu uspravni."));

        assertThrows(BadCredentialsException.class, () -> authService.authenticateUser(loginDTO));
        BadCredentialsException ex = assertThrows(
                BadCredentialsException.class,
                () -> authService.authenticateUser(loginDTO)
        );

        assertEquals("E-mail i/ili lozinka nisu uspravni.", ex.getMessage());

        verify(jwtUtil, never()).generateToken(any());
        verify(userService, never()).findByEmail(any());

    }
}