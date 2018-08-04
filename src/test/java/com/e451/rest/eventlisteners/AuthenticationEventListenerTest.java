package com.e451.rest.eventlisteners;

import com.e451.rest.domains.auth.FailedLoginAttempt;
import com.e451.rest.domains.user.User;
import com.e451.rest.services.AccountLockoutService;
import com.e451.rest.services.FailedLoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by l659598 on 7/19/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationEventListenerTest {

    @Mock
    private Authentication authentication;

    @Mock
    private WebAuthenticationDetails webAuthenticationDetails;

    @Mock
    private FailedLoginService failedLoginService;

    @Mock
    private AccountLockoutService accountLockoutService;

    @Mock
    private AuthenticationSuccessEvent authenticationSuccessEvent;

    @Mock
    private AbstractAuthenticationFailureEvent abstractAuthenticationFailureEvent;

    private User user;

    private AuthenticationEventListener authenticationEventListenerSpy;

    @Before
    public void setUp() {
        AuthenticationEventListener authenticationEventListener = new AuthenticationEventListener(failedLoginService, accountLockoutService);
        this.authenticationEventListenerSpy = spy(authenticationEventListener);
        this.user = new User("id1", "test", "testName", "test@test.com", "Password1");

    }

    @Test
    public void whenOnApplicationEventAuthenticationSuccessThenCallOnAuthenticationSuccess() {
        doNothing().when(authenticationEventListenerSpy).onAuthenticationSuccess(any());
        authenticationEventListenerSpy.onApplicationEvent(authenticationSuccessEvent);
        verify(authenticationEventListenerSpy).onAuthenticationSuccess(any());
    }

    @Test
    public void whenOnApplicationEventAuthenticationFailureThenCallOnAuthenticationFailure() {
        doNothing().when(authenticationEventListenerSpy).onAuthenticationFailure(any());
        authenticationEventListenerSpy.onApplicationEvent(abstractAuthenticationFailureEvent);
        verify(authenticationEventListenerSpy).onAuthenticationFailure(any());
    }

    @Test
    public void whenOnAuthenticationSuccessProcessLoginSuccessIsCalled() {
        when(authenticationSuccessEvent.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        authenticationEventListenerSpy.onAuthenticationSuccess(authenticationSuccessEvent);

        verify(accountLockoutService).processLoginSuccess(user.getUsername());
    }

    @Test
    public void whenOnAuthenticationFailureCreateFailedLoginAndProcessLoginFailure() {
        when(abstractAuthenticationFailureEvent.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user.getUsername());
        when(authentication.getDetails()).thenReturn(webAuthenticationDetails);
        when(webAuthenticationDetails.getRemoteAddress()).thenReturn("127.0.0.1");

        authenticationEventListenerSpy.onAuthenticationFailure(abstractAuthenticationFailureEvent);

        verify(failedLoginService).createFailedLoginAttempt(any(FailedLoginAttempt.class));
        verify(accountLockoutService).processLoginFailure(user.getUsername(), "127.0.0.1");
    }
}
