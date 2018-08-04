package com.e451.rest.services;

import com.e451.rest.domains.auth.FailedLoginAttempt;

import java.util.Date;
import java.util.List;

/**
 * Created by l659598 on 7/18/2017.
 */
public interface FailedLoginService {
    List<FailedLoginAttempt> findByDateBetweenAndUsername(Date fromDate, Date toDate, String username);
    FailedLoginAttempt createFailedLoginAttempt(FailedLoginAttempt failedLoginAttempt);
    List<FailedLoginAttempt> updateFailedLoginAttempts(Iterable<FailedLoginAttempt> attempts);
}
