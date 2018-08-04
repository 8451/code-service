package com.e451.rest.domains.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by j747951 on 6/22/2017.
 */
public class JwtAuthToken extends AbstractAuthenticationToken {
    private final String token;
    private final Object principal;

    public JwtAuthToken(Collection<? extends GrantedAuthority> authorities, String token, Object principal) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getToken() {
        return  this.token;
    }
}
