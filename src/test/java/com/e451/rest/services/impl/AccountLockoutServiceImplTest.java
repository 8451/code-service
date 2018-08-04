package com.e451.rest.services.impl;

import com.e451.rest.domains.auth.FailedLoginAttempt;
import com.e451.rest.domains.user.User;
import com.e451.rest.repositories.UserRepository;
import com.e451.rest.services.AccountLockoutService;
import com.e451.rest.services.FailedLoginService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by l659598 on 7/18/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountLockoutServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FailedLoginService failedLoginService;

    private AccountLockoutService accountLockoutService;

    private User mockUser;

    private List<FailedLoginAttempt> failedLoginAttempts;

    @Before
    public void setup() {
        this.accountLockoutService = new AccountLockoutServiceImpl(userRepository, failedLoginService, 600, 5);
        this.mockUser = new User("id1", "Test", "TestName", "Test@Test.com", "TestWord");
        this.failedLoginAttempts = Arrays.asList(
                new FailedLoginAttempt(),
                new FailedLoginAttempt(),
                new FailedLoginAttempt(),
                new FailedLoginAttempt(),
                new FailedLoginAttempt(),
                new FailedLoginAttempt(),
                new FailedLoginAttempt(),
                new FailedLoginAttempt()
        );
    }

    @Test
    public void whenProcessFailedLoginLessThanLimit_doNothing() {
        for (int i = 0; i < 4; i++) {
            failedLoginAttempts.get(i).setActive(false);
        }

        when(failedLoginService.findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(failedLoginAttempts);

        accountLockoutService.processLoginFailure("Test@Test.com", "mockIP");

        verifyZeroInteractions(userRepository);
    }

    @Test
    public void whenProcessFailedLoginGreaterThanLimit_updateUser() {
        when(failedLoginService.findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(this.failedLoginAttempts);
        when(userRepository.findByUsername(any(String.class))).thenReturn(mockUser);

        accountLockoutService.processLoginFailure("test@test.com", "mockIP");

        verify(userRepository).save(any(User.class));
        Assert.assertEquals(false, mockUser.isAccountNonLocked());
    }

    @Test
    public void whenAccountCanLoginAndIsLocked_updateUser() {
        when(failedLoginService.findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(new ArrayList<FailedLoginAttempt>());
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        mockUser.setLocked(true);

        Boolean canLogin = accountLockoutService.canAccountLogin(mockUser);

        verify(userRepository).save(any(User.class));
        Assert.assertEquals(true, canLogin);
    }

    @Test
    public void whenAccountCannotLoginAndIsLocked_doNothing() {
        when(failedLoginService.findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(failedLoginAttempts);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        mockUser.setLocked(true);

        Boolean canLogin = accountLockoutService.canAccountLogin(mockUser);

        verifyZeroInteractions(userRepository);
        Assert.assertEquals(false, canLogin);
    }

    @Test
    public void whenProcessLoginSuccess_updateFailedLoginAttempts() {
        when(failedLoginService.findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(failedLoginAttempts);

        accountLockoutService.processLoginSuccess(mockUser.getUsername());

        verify(failedLoginService).updateFailedLoginAttempts(any());
        Assert.assertTrue(failedLoginAttempts.stream().allMatch(attempt -> !attempt.isActive()));
    }

    @Test
    public void whenGetRecentLoginAttempts_returnListOfAttempts() {
        when(failedLoginService.findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(failedLoginAttempts);

        List<FailedLoginAttempt> actual = accountLockoutService.getRecentLoginAttempts(mockUser.getUsername());

        verify(failedLoginService).findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class));
        Assert.assertEquals(failedLoginAttempts.size(), actual.size());
    }


}
