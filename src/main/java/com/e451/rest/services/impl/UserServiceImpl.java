package com.e451.rest.services.impl;

import com.e451.rest.domains.user.User;
import com.e451.rest.repositories.UserRepository;
import com.e451.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by l659598 on 6/19/2017.
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User createUser(User user) {
        return userRepository.insert(user);
    }
}
