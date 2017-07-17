package com.e451.rest.services;

import com.e451.rest.domains.user.User;
import com.e451.rest.domains.user.UserVerification;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

/**
 * Created by l659598 on 6/19/2017.
 */
public interface UserService extends UserDetailsService {
    User createUser(User user) throws Exception;
    User updateUser(User user) throws Exception;
    User updateUser(UserVerification userVerification) throws Exception;
    void activateUser(String guid) throws Exception;
    void notifyUser(User user);
}
