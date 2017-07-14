package com.e451.rest.services.impl;

import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.domains.email.RegistrationEmailMessage;
import com.e451.rest.domains.user.User;
import com.e451.rest.repositories.UserRepository;
import com.e451.rest.services.MailService;
import com.e451.rest.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
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

    @Mock
    private MailService mailService;

    @Mock
    private PasswordEncoder encoder;

    @Before
    public void setup() {
        this.userService = new UserServiceImpl(userRepository, mailService, "test/api/v1");

        users = Arrays.asList(
                new User("id1", "Liz", "Conrad", "liz@conrad.com", "passw0rd!"),
                new User("id2", "Jacob", "Tucker", "jacob@tucker.com", "dr0wssap!")
        );
    }

    @Test
    public void whenCreateUser_returnNewUser() {
        User user = users.get(0);
        User result = null;

        Mockito.doNothing().when(mailService).sendEmail(any(DirectEmailMessage.class));
        when(userRepository.insert(user)).thenReturn(user);
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());

        try {
            result = userService.createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(user, result);
        Assert.assertNotNull(user.getId());
        Assert.assertNotNull(user.getFirstName());
        Assert.assertNotNull(user.getLastName());
        Assert.assertNotNull(user.getUsername());
        Assert.assertNotNull(user.getPassword());
        Assert.assertNotNull(user.getActivationGuid());
    }

    @Test
    public void whenLoadByUsername_returnUser() {
        User user = users.get(0);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        User result = new User();
        try {
            result = (User) userService.loadUserByUsername(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(user, result);
    }

    @Test
    public void whenActivateUser_enabledIsTrue() {
        User user = users.get(0);

        when(userRepository.findByActivationGuid(user.getActivationGuid())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        try {
            userService.activateUser(user.getActivationGuid());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(user.isEnabled());
    }

    @Test
    public void whenNotifyUser_mailServiceSendsMessage() {
        User user = users.get(0);

        Mockito.doNothing().when(mailService).sendEmail(any());

        userService.notifyUser(user);

        verify(mailService).sendEmail(any());
    }

    @Test
    public void whenUpdateUser_returnsUpdatedUser() {
        User user = users.get(0);
        User result = new User();
        String originalFirstName = user.getFirstName();
        user.setFirstName("newFirstName");
        when(userRepository.save(user)).thenReturn(user);
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());
        try {
            result = userService.updateUser(user);
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

        verify(userRepository).save(user);
        Assert.assertEquals(user, result);
        Assert.assertNotEquals(originalFirstName, result.getFirstName());
    }

}
