package com.e451.rest.domains.auth;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by l659598 on 7/18/2017.
 */
public class FailedLoginAttempt {
    @Id
    private String id;
    private String username;
    private String ipAddress;
    private Date date;
    private boolean active;

    public FailedLoginAttempt() {
        this.active = true;
    }

    public FailedLoginAttempt(String username, String ipAddress, Date date) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.date = date;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
