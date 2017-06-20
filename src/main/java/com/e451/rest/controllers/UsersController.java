package com.e451.rest.controllers;

import com.e451.rest.domains.user.User;
import com.e451.rest.domains.user.UserResponse;
import com.e451.rest.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Created by l659598 on 6/19/2017.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UsersController {
    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@RequestBody User user) {
        UserResponse userResponse = new UserResponse();

        logger.info("createUser request received");

        try {
            userResponse.setUsers(Arrays.asList(userService.createUser(user)));
            logger.info("createUser request processed");
        } catch (Exception ex) {
            logger.error("createUser encountered error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

}