package com.e451.rest.services.impl;

import com.e451.rest.domains.InvalidPasswordException;
import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.domains.email.ForgotPasswordEmailMessage;
import com.e451.rest.domains.email.RegistrationEmailMessage;
import com.e451.rest.domains.user.ResetForgottenPasswordRequest;
import com.e451.rest.domains.user.User;
import com.e451.rest.domains.user.UserVerification;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private UserService userService;
    private List<User> users;

    private PasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailService mailService;

    @Before
    public void setup() {
        this.userService = new UserServiceImpl(userRepository, mailService, "test/api/v1", 600L);
        this.encoder = ((UserServiceImpl)userService).passwordEncoder();

        users = Arrays.asList(
                new User("id1", "Liz", "Conrad", "liz@conrad.com", "passw0rd!"),
                new User("id2", "Jacob", "Tucker", "jacob@tucker.com", "dr0wssap!")
        );
    }

    @Test
    public void whenGetUsers_returnAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(users);

        List<User> serviceResponse = userService.getUsers();

        Assert.assertEquals(users.size(), serviceResponse.size());
    }

    @Test
    public void whenGetUsers_returnPageOfUsers() throws Exception {
        Pageable page = new PageRequest(0, 20);
        when(userRepository.findAll(page)).thenReturn(new PageImpl(this.users));

        Page<User> serviceResponse = userService.getUsers(page);

        Assert.assertEquals(users.size(), serviceResponse.getContent().size());
    }


    @Test
    public void whenCreateUser_returnNewUser() {
        User user = users.get(0);
        User result = null;

        Mockito.doNothing().when(mailService).sendEmail(any(DirectEmailMessage.class));
        when(userRepository.insert(user)).thenReturn(user);

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

    @Test(expected = Exception.class)
    public void whenCreateUserWithSameUsername_throwsException() throws Exception {
        User user = users.get(0);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        userService.createUser(user);
    }

    @Test
    public void whenUnlockUser_UserIsSavedByRepo() throws Exception {
        User user = users.get(0);
        when(userRepository.findOne(any(String.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.unlockUser(user);

        verify(userRepository).save(any(User.class));
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
    public void whenActivateUser_enabledIsTrue() throws Exception {
        User user = users.get(0);

        when(userRepository.findByActivationGuid(user.getActivationGuid())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        userService.activateUser(user.getActivationGuid());
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
    public void whenUpdateUser_returnsUpdatedUser() throws Exception {
        User user = users.get(0);
        User result;

        when(userRepository.findOne(user.getId())).thenReturn(user);

        String originalFirstName = user.getFirstName();
        user.setFirstName("newFirstName");
        when(userRepository.save(any(User.class))).thenReturn(user);

        result = userService.updateUser(user);

        verify(userRepository).save(any(User.class));
        Assert.assertEquals(user, result);
        Assert.assertNotEquals(originalFirstName, result.getFirstName());
    }

    @Test
    public void whenUpdateUserVerification_returnsUpdatedUser() throws Exception {
        User user = users.get(0);
        User newUser = new User("id1", "Liz", "Conrad", "liz@conrad.com", "Passw0rd!");

        UserVerification userVerification = new UserVerification();
        userVerification.setUser(newUser);
        userVerification.setCurrentPassword(user.getPassword());

        user.setPassword(encoder.encode(user.getPassword()));

        when(userRepository.findOne(user.getId())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(userVerification);

        verify(userRepository).save(any(User.class));
        Assert.assertEquals(user, result);
    }

    @Test(expected = InvalidPasswordException.class)
    public void whenUpdateUserVerificationNonMatchingPassword_thenThrowInvalidPasswordException() throws Exception {
        User user = users.get(0);
        User newUser = new User("id1", "Liz", "Conrad", "liz@conrad.com", "Passw0rd!");

        UserVerification userVerification = new UserVerification();
        userVerification.setUser(newUser);
        userVerification.setCurrentPassword("nonMatchingPass");

        user.setPassword(encoder.encode(user.getPassword()));

        when(userRepository.findOne(any(String.class))).thenReturn(user);

        userService.updateUser(userVerification);
    }

    @Test(expected = InvalidPasswordException.class)
    public void whenUpdateUserVerificationInvalidNewPassword_thenThrowInvalidPasswordException() throws Exception {
        User user = users.get(0);
        User newUser = new User("id1", "Liz", "Conrad", "liz@conrad.com", "invalidPass");
        UserVerification userVerification = new UserVerification();
        userVerification.setUser(newUser);
        userVerification.setCurrentPassword(user.getPassword());

        user.setPassword(encoder.encode(user.getPassword()));

        when(userRepository.findOne(any(String.class))).thenReturn(user);

        userService.updateUser(userVerification);
    }

    @Test
    public void whenDeleteUser_verifyDeleteIsCalled() {
        Mockito.doNothing().when(userRepository).delete("1");

        userService.deleteUser("1");

        verify(userRepository).delete("1");
    }

    @Test
    public void whenSearchUser_verifySearchUserIsCalled() throws Exception {
        when(userRepository.findByUsernameContainingIgnoreCase(any(Pageable.class), any(String.class))).thenReturn(new PageImpl<>(this.users));

        Pageable pageable = new PageRequest(0, 20);
        userService.searchUsers(pageable, "test");

        verify(userRepository).findByUsernameContainingIgnoreCase(pageable, "test");
    }

    @Test
    public void whenUserForgotPassword_verifySendEmailIsCalled() throws Exception {
        Mockito.doNothing().when(mailService).sendEmail(any(DirectEmailMessage.class));
        when(userRepository.save(any(User.class))).thenReturn(users.get(0));
        when(userRepository.findByUsername("username")).thenReturn(users.get(0));

        userService.userForgotPassword("username");

        verify(mailService).sendEmail(any(ForgotPasswordEmailMessage.class));
    }

    @Test
    public void whenUserForgotPassword_verifyUserHasResetGuidAndDate() throws Exception {
        User user = users.get(0);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.userForgotPassword(user.getUsername());

        Assert.assertNotNull(user.getResetPasswordGuid());
        Assert.assertNotNull(user.getResetPasswordSentDate());
        verify(userRepository).save(user);
    }

    @Test
    public void whenUserResetsForgottenPasswordSuccess_verifyUserHasUpdatedPasswordAndResetFieldsAreNull() throws Exception {
        User user = users.get(0);
        user.setResetPasswordGuid("1234");
        user.setResetPasswordSentDate(new Date());
        when(userRepository.findByResetPasswordGuid("1234")).thenReturn(user);

        ResetForgottenPasswordRequest request = new ResetForgottenPasswordRequest(user.getUsername(), user.getResetPasswordGuid());
        request.setNewPassword("Password1!");
        userService.resetForgottenPassword(request);

        Assert.assertNull(user.getResetPasswordSentDate());
        Assert.assertNull(user.getResetPasswordGuid());
        Assert.assertTrue(encoder.matches(request.getNewPassword(), user.getPassword()));
    }

    @Test(expected = BadCredentialsException.class)
    public void whenUserResetsForgottenPasswordExpired_verifyBadCredentialsExceptionThrown() throws Exception {
        User user = users.get(0);
        user.setResetPasswordGuid("1234");
        user.setResetPasswordSentDate(new Date(0L));
        when(userRepository.findByResetPasswordGuid("1234")).thenReturn(user);

        ResetForgottenPasswordRequest request = new ResetForgottenPasswordRequest(user.getUsername(), user.getResetPasswordGuid());
        request.setNewPassword("Password1!");
        userService.resetForgottenPassword(request);
    }

    @Test(expected = BadCredentialsException.class)
    public void whenUserResetsForgottenPasswordInfoDoesntMatch_verifyBadCredentialExceptionThrown() throws  Exception {
        User user = users.get(0);
        user.setResetPasswordGuid("1234");
        user.setResetPasswordSentDate(new Date());
        when(userRepository.findByResetPasswordGuid("1234")).thenReturn(user);

        ResetForgottenPasswordRequest request = new ResetForgottenPasswordRequest(user.getUsername() + "!", user.getResetPasswordGuid());
        request.setNewPassword("Password1!");
        userService.resetForgottenPassword(request);
    }

    @Test(expected = InvalidPasswordException.class)
    public void whenUserResetsPasswordPasswordInvalid_verifyInvalidPasswordException() throws Exception {
        User user = users.get(0);
        user.setResetPasswordGuid("1234");
        user.setResetPasswordSentDate(new Date());
        when(userRepository.findByResetPasswordGuid("1234")).thenReturn(user);

        ResetForgottenPasswordRequest request = new ResetForgottenPasswordRequest(user.getUsername(), user.getResetPasswordGuid());
        request.setNewPassword("password");
        userService.resetForgottenPassword(request);
    }

}
