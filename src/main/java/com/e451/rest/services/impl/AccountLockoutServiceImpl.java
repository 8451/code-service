package com.e451.rest.services.impl;

import com.e451.rest.domains.auth.FailedLoginAttempt;
import com.e451.rest.domains.user.User;
import com.e451.rest.repositories.UserRepository;
import com.e451.rest.services.AccountLockoutService;
import com.e451.rest.services.FailedLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by l659598 on 7/18/2017.
 */
@Service
public class AccountLockoutServiceImpl implements AccountLockoutService {

    private UserRepository userRepository;
    private FailedLoginService failedLoginService;
    private Integer timeout;
    private Integer attemptLimit;

    @Autowired
    public AccountLockoutServiceImpl(
            UserRepository userRepository,
            FailedLoginService failedLoginService,
            @Value("${code.lockout.timeout}") Integer timeout,
            @Value("${code.lockout.attempt-limit}") Integer attemptLimit) {

        this.userRepository = userRepository;
        this.failedLoginService = failedLoginService;
        this.timeout = timeout;
        this.attemptLimit = attemptLimit;
    }

    @Override
    public Boolean canAccountLogin(final User user) {
        if (!user.isAccountNonLocked()) {
            String username = user.getUsername();
            List<FailedLoginAttempt> attempts = getRecentLoginAttempts(username);

            if(attempts.size() == 0 || attempts.stream().allMatch(attempt -> !attempt.isActive())) {
               user.setLocked(false);
               userRepository.save(user);
            }
        }

        return user.isAccountNonLocked();
    }

    @Override
    public void processLoginFailure(String username, String ipAddress) {
        List<FailedLoginAttempt> failedAttempts = getRecentLoginAttempts(username);

        if (failedAttempts.stream().filter(attempt -> attempt.isActive()).count() >= attemptLimit) {
            User user = userRepository.findByUsername(username);
            user.setLocked(true);
            userRepository.save(user);
        }
    }

    @Override
    public void processLoginSuccess(String username) {
        List<FailedLoginAttempt> failedAttempts = getRecentLoginAttempts(username);

        for (FailedLoginAttempt failedLoginAttempt : failedAttempts) {
            failedLoginAttempt.setActive(false);
        }

        failedLoginService.updateFailedLoginAttempts(failedAttempts);
    }

    @Override
    public List<FailedLoginAttempt> getRecentLoginAttempts(String username) {
        Date currentDate = new Date();
        Date timeoutDate = new Date(System.currentTimeMillis() - 1000 * timeout);

        return failedLoginService.findByDateBetweenAndUsername(timeoutDate, currentDate, username);
    }

}
