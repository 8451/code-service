package com.e451.rest.controllers;

import com.e451.rest.domains.InvalidPasswordException;
import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.user.ResetForgottenPasswordRequest;
import com.e451.rest.domains.user.User;
import com.e451.rest.domains.user.UserResponse;
import com.e451.rest.domains.user.UserVerification;
import com.e451.rest.services.AuthService;
import com.e451.rest.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by l659598 on 6/19/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest {

    private UsersController usersController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;


    private List<User> users;

    @Before
    public void setup() {
        this.usersController = new UsersController(userService, authService);

        users = Arrays.asList(
                new User("id1","Liz", "Conrad", "liz@conrad.com", "passw0rd"),
                new User("id2","Jacob", "Tucker", "jacob@tucker.com", "dr0wssap")
        );
    }

    @Test
    public void whenGetUsers_returnListOfUsers() throws Exception {
        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<UserResponse> response = usersController.getUsers();

        Assert.assertEquals(users.size(), response.getBody().getUsers().size());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenGetUsersPageable_returnListOfUsers() throws Exception {
        Pageable page = new PageRequest(0, 20);
        Page pageResponse = new PageImpl<User>(this.users);
        when(userService.getUsers(any())).thenReturn(pageResponse);

        ResponseEntity<UserResponse> response = usersController.getUsers(0, 20, "title");

        Assert.assertEquals(this.users.size(), response.getBody().getUsers().size());
        Assert.assertEquals(pageResponse.getTotalElements(), (long) response.getBody().getPaginationTotalElements());
    }

    @Test
    public void whenCreateUser_returnsListOfSingleUser() throws Exception {
        User user = users.get(0);

        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<UserResponse> response = usersController.createUser(user);

        Assert.assertEquals(1, response.getBody().getUsers().size());
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void whenCreateUser_notifyUser() throws Exception {
        User user = users.get(0);

        when(userService.createUser(user)).thenReturn(user);
        Mockito.doNothing().when(userService).notifyUser(user);

        usersController.createUser(user);

        verify(userService).notifyUser(user);

    }

    @Test
    public void whenCreateUser_UserServiceThrowsException_returnInternalServerError() throws Exception {
        User user = users.get(1);

        when(userService.createUser(user)).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<UserResponse> responseEntity = usersController.createUser(user);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void whenCreateUser_UserServiceThrowsException_noEmailSent() throws Exception {
        User user = users.get(0);

        when(userService.createUser(user)).thenThrow(new Exception());
        Mockito.doNothing().when(userService).notifyUser(user);

        usersController.createUser(user);

        verify(userService, never()).notifyUser(user);
    }

    @Test
    public void whenGetActiveUser_returnUser() throws Exception {
        User user = users.get(0);
        when(authService.getActiveUser()).thenReturn(user);
        ResponseEntity<UserResponse> userResponse = usersController.getActiveUser();
        Assert.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        Assert.assertEquals(user, userResponse.getBody().getUsers().get(0));
    }

    @Test
    public void whenActivateUser_returnOK() throws Exception {
        User user = users.get(0);

        Mockito.doNothing().when(userService).activateUser(user.getActivationGuid());

        ResponseEntity response = usersController.activateUser(user.getActivationGuid());

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenActivateUser_UserServiceThrowsException_returnInternalServerError() throws Exception {
        User user = users.get(0);

        Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).activateUser(user.getActivationGuid());

        ResponseEntity responseEntity = usersController.activateUser(user.getActivationGuid());

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void whenUpdateUser_returnsUpdatedUser() throws Exception {
        User user = users.get(0);
        user.setFirstName("newFirstName");

        when(userService.updateUser(user)).thenReturn(user);

        ResponseEntity<UserResponse> responseEntity = usersController.updateUser(user);

        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        Assert.assertEquals(user, responseEntity.getBody().getUsers().get(0));
    }

    @Test
    public void whenUpdateUser_UserServiceThrowsException_returnInternalServerError() throws Exception {
        User user = users.get(0);

        Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).updateUser(user);

        ResponseEntity responseEntity = usersController.updateUser(user);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void whenUpdateUserVerification_returnsUpdatedUser() throws Exception {
        User user = users.get(0);
        UserVerification userVerification = new UserVerification();
        userVerification.setUser(user);
        userVerification.setCurrentPassword("Password1");

        when(userService.updateUser(userVerification)).thenReturn(user);

        ResponseEntity<UserResponse> responseEntity = usersController.updateUser(userVerification);

        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        Assert.assertEquals(user, responseEntity.getBody().getUsers().get(0));
    }

    @Test
    public void whenUpdateUserVerification_UserServiceThrowsException_returnInternalServerError() throws Exception {
        User user = users.get(0);
        UserVerification userVerification = new UserVerification();
        userVerification.setUser(user);
        userVerification.setCurrentPassword("Password1");

        Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).updateUser(userVerification);

        ResponseEntity responseEntity = usersController.updateUser(userVerification);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void whenDeleteUser_returnSuccess()  {

        Mockito.doNothing().when(userService).deleteUser("1");

        ResponseEntity response = usersController.deleteUser("1");

        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void whenDeleteUser_UserServiceThrowsException_returnsInternalServerError() {
        Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).deleteUser("1");

        ResponseEntity response = usersController.deleteUser("1");

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void whenUnlockUser_returnSuccess() throws Exception {
        User user = users.get(0);
        when(userService.unlockUser(any(User.class))).thenReturn(user);
        ResponseEntity<UserResponse> response = usersController.unlockUser(user);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(user, response.getBody().getUsers().get(0));
    }

    @Test
    public void whenUnlockUser_UserServiceThrowsExcepetion_returnsInternalServerError() throws Exception {
        Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).unlockUser(any(User.class));
        ResponseEntity response = usersController.unlockUser(users.get(0));
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void whenSearchUsers_returnListOfUsers() throws Exception {
        when(userService.searchUsers(any(Pageable.class), eq("test"))).thenReturn(new PageImpl<>(this.users));
        ResponseEntity<UserResponse> response = usersController.searchUsers(0, 20, "lastName", "test");
        Assert.assertEquals(2L, (long)response.getBody().getPaginationTotalElements());
    }

    @Test
    public void whenForgotPassword_returnSuccess() throws Exception {
        Mockito.doNothing().when(userService).userForgotPassword("test@user.com");
        ResponseEntity responseEntity = usersController.forgotPassword("test@user.com");
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void whenForgotPassword_UserServiceThrowsException_returnsInternalServerError() throws Exception {
        Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).userForgotPassword("test@user.com");
        ResponseEntity responseEntity = usersController.forgotPassword("test@user.com");
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void whenResetForgottenPassword_returnsOK() throws Exception {
        ResetForgottenPasswordRequest request = new ResetForgottenPasswordRequest("username", "guid");
        Mockito.doNothing().when(userService).resetForgottenPassword(request);

        ResponseEntity response = usersController.resetForgottenPassword(request);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenResetForgottenPassword_userServiceThrowsInvalidException_returnsUnauthorized() throws Exception {
        ResetForgottenPasswordRequest request = new ResetForgottenPasswordRequest("username", "guid");
        Mockito.doThrow(new InvalidPasswordException()).when(userService).resetForgottenPassword(request);

        ResponseEntity response = usersController.resetForgottenPassword(request);

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void whenResetForgottenPassword_userServiceThrowsException_returnsInternalServerError() throws Exception {
        ResetForgottenPasswordRequest request = new ResetForgottenPasswordRequest("username", "guid");
        Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).resetForgottenPassword(request);

        ResponseEntity response = usersController.resetForgottenPassword(request);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
