package com.e451.rest.domains.user;

import java.util.List;

/**
 * Created by l659598 on 6/19/2017.
 */
public class UserResponse {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

