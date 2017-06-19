package com.e451.rest.services.impl;

import com.e451.rest.domains.user.User;
import com.e451.rest.repositories.UserRepository;
import com.e451.rest.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by l659598 on 6/19/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private UserService userService;
    private List<User> users;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        this.userService = new UserServiceImpl(userRepository);
        users = Arrays.asList(
                new User("id1", "Liz", "Conrad", "liz@conrad.com", "passw0rd", true),
                new User("id2","Jacob", "Tucker", "jacob@tucker.com", "dr0wssap", true)
        );
    }

    @Test
    public void whenCreateUser_returnNewUser() {
        User user =  users.get(0);

        when(userRepository.insert(user)).thenReturn(user);

        User result = userService.createUser(user);

        Assert.assertEquals(user, result);
        Assert.assertNotNull(user.getId());
        Assert.assertNotNull(user.getFirstName());
        Assert.assertNotNull(user.getLastName());
        Assert.assertNotNull(user.getEmail());
        Assert.assertNotNull(user.getPassword());
        Assert.assertNotNull(user.getActivationGuid());

        // TODO: add user properties.
    }

}

