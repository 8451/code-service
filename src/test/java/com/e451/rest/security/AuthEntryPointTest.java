package com.e451.rest.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.verify;


/**
 * Created by j747951 on 6/21/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private AuthEntryPoint authEntryPoint;

    @Before
    public void setup() {
        authEntryPoint = new AuthEntryPoint();
    }

    @Test
    public void commence_throwsExceptionWhenUnauthorized() {
        try {
            authEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException("error"));
            verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } catch(Exception ex) {
            // do nothing
        } finally {
        }
    }
}
