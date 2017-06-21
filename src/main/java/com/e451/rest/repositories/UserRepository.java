package com.e451.rest.repositories;

import com.e451.rest.domains.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by l659598 on 6/19/2017.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    User findByActivationGuid(String guid);
}
