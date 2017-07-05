package com.e451.rest.services.impl;

import com.e451.rest.domains.user.User;
import com.e451.rest.services.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by j747951 on 6/26/2017.
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public User getActiveUser() {
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User;
    }
}
