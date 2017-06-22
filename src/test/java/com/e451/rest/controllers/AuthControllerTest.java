package com.e451.rest.controllers;

import com.e451.rest.domains.auth.AuthenticationRequest;
import com.e451.rest.domains.user.User;
import com.e451.rest.security.JwtTokenUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by j747951 on 6/22/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {
    private AuthController authController;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;


    private String tokenHeader;
    private User user;

    @Before
    public void setup() {
        tokenHeader = "header";
        authController = new AuthController(tokenHeader, userDetailsService, authenticationManager, jwtTokenUtil);
        user = new User("id1", "fname", "lname", "email@gmail.com", "Password1");
    }

    @Test
    public void whenCreateAuthenticationToken_ReturnsValidToken() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        authenticationRequest.setEmail("email@gmail.com");
        authenticationRequest.setPassword("Password1");

    }
}
