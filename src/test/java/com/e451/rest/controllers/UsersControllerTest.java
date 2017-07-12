package com.e451.rest.controllers;

import com.e451.rest.domains.assessment.Assessment;
import com.e451.rest.domains.user.User;
import com.e451.rest.domains.user.UserResponse;
import com.e451.rest.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.AssertTrue;
import java.util.Arrays;
import java.util.List;

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


    private List<User> users;

    @Before
    public void setup() {
        this.usersController = new UsersController(userService);

        users = Arrays.asList(
                new User("id1","Liz", "Conrad", "liz@conrad.com", "passw0rd"),
                new User("id2","Jacob", "Tucker", "jacob@tucker.com", "dr0wssap")
        );
    }

    @Test
    public void whenCreateUser_returnsListOfSingleUser() {
        User user = users.get(0);

        try {
            when(userService.createUser(user)).thenReturn(user);
        }
        catch (Exception ex) {
            Assert.assertTrue(false);
        }


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
    public void whenCreateUser_UserServiceThrowsException_returnInternalServerError() {
        User user = users.get(1);

        try {
            when(userService.createUser(user)).thenThrow(new RecoverableDataAccessException("error"));
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }


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
    public void whenGetUserById_returnUser() {
        User user = users.get(0);

        try {
            when(userService.getUserById(user.getId())).thenReturn(user);
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }
        ResponseEntity<UserResponse> userResponse = usersController.getUserById(user.getId());
        Assert.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        Assert.assertEquals(user, userResponse.getBody().getUsers().get(0));
    }

    @Test
    public void whenActivateUser_returnOK() {
        User user = users.get(0);

        try {
            Mockito.doNothing().when(userService).activateUser(user.getActivationGuid());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity response = usersController.activateUser(user.getActivationGuid());

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenActivateUser_UserServiceThrowsException_returnInternalServerError() {
        User user = users.get(0);

        try {
            Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).activateUser(user.getActivationGuid());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity responseEntity = usersController.activateUser(user.getActivationGuid());

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void whenUpdateUser_returnsUpdatedUser() {
        User user = users.get(0);
        user.setFirstName("newFirstName");

        try {
            when(userService.updateUser(user)).thenReturn(user);
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

        ResponseEntity<UserResponse> responseEntity = usersController.updateUser(user);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(user, responseEntity.getBody().getUsers().get(0));
    }
}
