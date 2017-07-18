package com.e451.rest.domains.user;

/**
 * Created by l659598 on 7/17/2017.
 */
public class UserVerification {
    private User user;
    private String currentPassword;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
