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
    public void whenGetUsers_returnListOfUsers() throws Exception {
        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<UserResponse> response = usersController.getUsers();

        Assert.assertEquals(users.size(), response.getBody().getUsers().size());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
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
    public void whenDeleteQuestion_returnSuccess()  {

        Mockito.doNothing().when(userService).deleteUser("1");

        ResponseEntity response = usersController.deleteUser("1");

        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void whenDeleteQuestion_QuestionServiceThrowsException_returnsInternalServerError() {
        Mockito.doThrow(new RecoverableDataAccessException("error")).when(userService).deleteUser("1");

        ResponseEntity response = usersController.deleteUser("1");

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
