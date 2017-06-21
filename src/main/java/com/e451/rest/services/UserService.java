package com.e451.rest.services;

import com.e451.rest.domains.user.User;

import java.util.UUID;

/**
 * Created by l659598 on 6/19/2017.
 */
public interface UserService {
    User createUser(User user) throws Exception;
    void activateUser(String guid) throws Exception;
}
