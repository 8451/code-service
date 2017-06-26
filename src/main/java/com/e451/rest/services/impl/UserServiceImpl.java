package com.e451.rest.services.impl;

import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.domains.email.RegistrationEmailMessage;
import com.e451.rest.domains.user.User;
import com.e451.rest.repositories.UserRepository;
import com.e451.rest.services.MailService;
import com.e451.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by l659598 on 6/19/2017.
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private MailService mailService;
    private String codeWebAddress;

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
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
    public User createUser(User user) throws Exception {
        DirectEmailMessage message = new RegistrationEmailMessage(user, codeWebAddress);
        if (!isPasswordValid(user.getPassword()))
            throw new Exception();

        user.setPassword(passwordEncoder().encode(user.getPassword()));

        mailService.sendEmail(message);

        return userRepository.insert(user);
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
    public void activateUser(String guid) {
        User user = userRepository.findByActivationGuid(guid);

        user.setEnabled(true);

        userRepository.save(user);
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
