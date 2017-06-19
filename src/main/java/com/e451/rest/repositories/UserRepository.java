package com.e451.rest.repositories;

import com.e451.rest.domains.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by l659598 on 6/19/2017.
 */
public interface UserRepository extends MongoRepository<User, String> {
}
