package com.e451.rest.controllers;

import com.e451.rest.domains.auth.AuthenticationRequest;
import com.e451.rest.domains.auth.AuthenticationResponse;
import com.e451.rest.domains.user.User;
import com.e451.rest.security.JwtTokenUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

/**
 * Created by j747951 on 6/22/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {
    private AuthController authController;

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private AuthenticationRequest authenticationRequest;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private HttpServletRequest servletRequest;


    private String tokenHeader;
    private User user;

    @Before
    public void setup() {
        tokenHeader = "header";
        authController = new AuthController(tokenHeader, userDetailsService, authenticationManager, jwtTokenUtil);
        when(servletRequest.getHeader(tokenHeader)).thenReturn("imatoken");
        when(jwtTokenUtil.generateToken(null)).thenReturn("imatoken");
        when(jwtTokenUtil.refreshToken("imatoken")).thenReturn("newtoken");
        user = new User("id1", "fname", "lname", "email@gmail.com", "Password1");
    }

    @Test
    public void whenCreateAuthenticationToken_ReturnsValidToken() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        ResponseEntity<AuthenticationResponse> response = authController.createAuthenticationToken(servletRequest, authenticationRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals("imatoken", response.getBody().getToken());
    }

    @Test
    public void whenRefreshAndGetAuthenticationToken_returnsRefreshedToken() {
        when(jwtTokenUtil.canTokenBeRefreshed("imatoken")).thenReturn(true);
        ResponseEntity<AuthenticationResponse> response = authController.refreshAndGetAuthenticationToken(servletRequest);
        Assert.assertEquals("newtoken", response.getBody().getToken());
    }

    @Test
    public void whenRefreshAndGetAuthenticationTokenBeforeTokenCanBeRefreshed_returnsBadRequest() {
        when(jwtTokenUtil.canTokenBeRefreshed("imatoken")).thenReturn(false);
        ResponseEntity<AuthenticationResponse> response = authController.refreshAndGetAuthenticationToken(servletRequest);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}