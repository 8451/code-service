package com.e451.rest.services;

import com.e451.rest.domains.InvalidPasswordException;
import com.e451.rest.domains.user.ResetForgottenPasswordRequest;
import com.e451.rest.domains.user.User;
import com.e451.rest.domains.user.UserVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

/**
 * Created by l659598 on 6/19/2017.
 */
public interface UserService extends UserDetailsService {
    List<User> getUsers() throws Exception;
    Page<User> getUsers(Pageable pageable) throws Exception;
    Page<User> searchUsers(Pageable pageable, String searchString) throws Exception;
    User createUser(User user) throws Exception;
    User unlockUser(User user) throws Exception;
    User updateUser(User user) throws Exception;
    User updateUser(UserVerification userVerification) throws Exception;
    void deleteUser(String id);
    void activateUser(String guid) throws Exception;
    void userForgotPassword(String username) throws Exception;
    void resetForgottenPassword(ResetForgottenPasswordRequest request) throws BadCredentialsException, InvalidPasswordException;
    void notifyUser(User user);
}
