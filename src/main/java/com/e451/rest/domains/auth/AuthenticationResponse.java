package com.e451.rest.domains.auth;

import java.io.Serializable;

/**
 * Created by j747951 on 6/21/2017.
 */
public class AuthenticationResponse implements Serializable {

    private final String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
