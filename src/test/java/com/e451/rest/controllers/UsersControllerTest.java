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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

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
                new User("id1","Liz", "Conrad", "liz@conrad.com", "passw0rd", true),
                new User("id2","Jacob", "Tucker", "jacob@tucker.com", "dr0wssap", true)
        );
    }

    @Test
    public void whenCreateUser_returnsListOfSingleUser() {
        User user = users.get(0);

        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<UserResponse> response = usersController.createUser(user);

        Assert.assertEquals(1, response.getBody().getUsers().size());
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void whenCreateUser_UserServiceThrowsException_returnInternalServerError() {
        User user = users.get(1);

        when(userService.createUser(user)).thenThrow(new RecoverableDataAccessException("error"));

        ResponseEntity<UserResponse> responseEntity = usersController.createUser(user);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
