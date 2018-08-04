package com.e451.rest.repositories;

import com.e451.rest.domains.auth.FailedLoginAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by l659598 on 7/18/2017.
 */
public interface FailedLoginRepository extends MongoRepository<FailedLoginAttempt, String> {
    List<FailedLoginAttempt> findByDateBetweenAndUsername(Date fromDate, Date toDate, String username);
}
