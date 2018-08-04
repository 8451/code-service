package com.e451.rest.services.impl;

import com.e451.rest.domains.auth.FailedLoginAttempt;
import com.e451.rest.repositories.FailedLoginRepository;
import com.e451.rest.services.FailedLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by l659598 on 7/18/2017.
 */
@Service
public class FailedLoginServiceImpl implements FailedLoginService {

    @Autowired
    private FailedLoginRepository failedLoginRepository;

    public FailedLoginServiceImpl(FailedLoginRepository failedLoginRepository) {
        this.failedLoginRepository = failedLoginRepository;
    }

    @Override
    public List<FailedLoginAttempt> findByDateBetweenAndUsername(Date fromDate, Date toDate, String username) {
        return failedLoginRepository.findByDateBetweenAndUsername(fromDate, toDate, username);
    }

    @Override
    public FailedLoginAttempt createFailedLoginAttempt(FailedLoginAttempt failedLoginAttempt) {
        return failedLoginRepository.save(failedLoginAttempt);
    }

    @Override
    public List<FailedLoginAttempt> updateFailedLoginAttempts(Iterable<FailedLoginAttempt> attempts) {
        return failedLoginRepository.save(attempts);
    }
}
