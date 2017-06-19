package com.e451.rest.repositories;

import com.e451.rest.domains.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by l659598 on 6/19/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private List<User> createdUsers;
    private List<User> users;

    @Before
    public void setup() {
        users = Arrays.asList(
                new User("id1", "Liz", "Conrad", "liz@conrad.com", "passw0rd", true),
                new User("id2", "Jacob", "Tucker", "jacob@tucker.com", "dr0wssap", true)
        );
        createdUsers = userRepository.insert(users);
    }

    public void teardown() {
        userRepository.deleteAll();
    }

    @Test
    public void createUsers_returnAllUsers() {
        Assert.assertEquals(users, createdUsers);
    }
}
