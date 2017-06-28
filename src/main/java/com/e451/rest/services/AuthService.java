package com.e451.rest.services;

import com.e451.rest.domains.user.User;

/**
 * Created by j747951 on 6/26/2017.
 */
public interface AuthService {
    User getActiveUser();
}
