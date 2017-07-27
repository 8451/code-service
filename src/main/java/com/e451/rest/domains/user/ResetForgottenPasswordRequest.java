package com.e451.rest.domains.user;

/**
 * Created by j747951 on 7/26/2017.
 */
public class ResetForgottenPasswordRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String resetGuid;
    private String newPassword;

    public ResetForgottenPasswordRequest(String firstName, String lastName, String username, String resetGuid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.resetGuid = resetGuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResetGuid() {
        return resetGuid;
    }

    public void setResetGuid(String resetGuid) {
        this.resetGuid = resetGuid;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ResetForgottenPasswordRequest() {
    }
}
