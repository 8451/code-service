package com.e451.rest.services.impl;

import com.e451.rest.domains.auth.FailedLoginAttempt;
import com.e451.rest.repositories.FailedLoginRepository;
import com.e451.rest.services.FailedLoginService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by l659598 on 7/18/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FailedLoginServiceImplTest {

    private FailedLoginService failedLoginService;

    @Mock
    private FailedLoginRepository failedLoginRepository;

    private List<FailedLoginAttempt> attempts;

    @Before
    public void setUp() {
        this.failedLoginService = new FailedLoginServiceImpl(failedLoginRepository);
        attempts = Arrays.asList(
                new FailedLoginAttempt("username", "127.0.0.1", new Date()),
                new FailedLoginAttempt("username1", "127.0.0.1", new Date())
        );
    }

    @Test
    public void whenFindDateBetweenAndUserReturnListOfFailedLoginAttempts() {
        when(failedLoginRepository.findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(attempts);

        Date fromDate = new Date();
        Date toDate = new Date();

        List<FailedLoginAttempt> actual = failedLoginService.findByDateBetweenAndUsername(fromDate, toDate, attempts.get(0).getUsername());

        verify(failedLoginRepository).findByDateBetweenAndUsername(any(Date.class), any(Date.class), any(String.class));
        Assert.assertEquals(attempts.size(), actual.size());

    }

    @Test
    public void whenCreateFailedLoginAttemptReturnFailedLoginAttempt() {
        when(failedLoginRepository.save(any(FailedLoginAttempt.class))).thenReturn(attempts.get(0));

        FailedLoginAttempt actual = failedLoginService.createFailedLoginAttempt(new FailedLoginAttempt());

        verify(failedLoginRepository).save(any(FailedLoginAttempt.class));
        Assert.assertEquals(attempts.get(0), actual);
    }

    @Test
    public void whenUpdateFailedLoginAttemptsReturnFailedLoginAttempts() {
        when(failedLoginRepository.save(any(Iterable.class))).thenReturn(attempts);

        List<FailedLoginAttempt> actual = failedLoginService.updateFailedLoginAttempts(attempts);

        verify(failedLoginRepository).save(any(Iterable.class));
        Assert.assertEquals(attempts.size(), actual.size());
    }


}
