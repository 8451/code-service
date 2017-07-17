package com.e451.rest.services.impl;

import com.e451.rest.domains.InvalidPasswordException;
import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.domains.email.RegistrationEmailMessage;
import com.e451.rest.domains.user.User;
import com.e451.rest.domains.user.UserVerification;
import com.e451.rest.repositories.UserRepository;
import com.e451.rest.services.MailService;
import com.e451.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by l659598 on 6/19/2017.
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private MailService mailService;
    private String codeWebAddress;
    private PasswordEncoder encoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        if(this.encoder == null) this.encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MailService mailService,
                           @Value("${code.web-ui-address}") String codeWebAddress) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.codeWebAddress = codeWebAddress;
    }

    @Override
    public List<User> getUsers() throws Exception {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getUsers(Pageable pageable) throws Exception {
        return userRepository.findAll(pageable);
    }

    @Override
    public User createUser(User user) throws Exception {
        if (!isPasswordValid(user.getPassword()))
            throw new InvalidPasswordException();

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.insert(user);
    }

    @Override
    public User updateUser(User user) throws Exception {
        User curUser = userRepository.findOne(user.getId());

        curUser.setFirstName(user.getFirstName());
        curUser.setLastName(user.getLastName());
        curUser.setUsername(user.getUsername());

        return userRepository.save(curUser);
    }

    @Override
    public User updateUser(UserVerification userVerification) throws Exception {
        User user = userRepository.findOne(userVerification.getUser().getId());

        if (!encoder.matches(userVerification.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (!isPasswordValid(userVerification.getUser().getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setFirstName(userVerification.getUser().getFirstName());
        user.setLastName(userVerification.getUser().getLastName());
        user.setUsername(userVerification.getUser().getUsername());
        user.setPassword(encoder.encode(userVerification.getUser().getPassword()));

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }

    @Override
    public void activateUser(String guid) throws Exception {
        User user = userRepository.findByActivationGuid(guid);

        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void notifyUser(User user) {
        DirectEmailMessage message = new RegistrationEmailMessage(user, codeWebAddress);
        mailService.sendEmail(message);
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8)
            return false;
        int count = 0;
        if (Pattern.matches(".*[A-Z].*", password))
            count++;
        if (Pattern.matches(".*[a-z].*", password))
            count++;
        if (Pattern.matches(".*[0-9].*", password))
            count++;
        if (Pattern.matches(".*([^\\w\\s]).*", password))
            count++;
        return count >= 3;
    }
}
