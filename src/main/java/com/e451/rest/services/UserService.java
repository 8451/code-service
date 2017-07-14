package com.e451.rest.services;

import com.e451.rest.domains.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

/**
 * Created by l659598 on 6/19/2017.
 */
public interface UserService extends UserDetailsService {
    List<User> getUsers() throws Exception;
    User createUser(User user) throws Exception;
    void deleteUser(String id);
    void activateUser(String guid) throws Exception;
    void notifyUser(User user);
}
