package com.e451.rest.domains.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Created by l659598 on 6/19/2017.
 */
@Document
@Component
public class User implements UserDetails {

    @Id
    private String id;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean enabled;

    private boolean locked;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Indexed
    private String activationGuid;

    @JsonIgnore
    @Indexed
    private String resetPasswordGuid;

    @JsonIgnore
    private Date resetPasswordSentDate;

    public User() {
        this.activationGuid = UUID.randomUUID().toString();
    }

    public User(String id, String firstName, String lastName, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.enabled = false;
        this.activationGuid = UUID.randomUUID().toString();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActivationGuid() {
        return activationGuid;
    }

    public void setActivationGuid(String activationGuid) {
        this.activationGuid = activationGuid;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getResetPasswordGuid() {
        return resetPasswordGuid;
    }

    public void setResetPasswordGuid(String resetPasswordGuid) {
        this.resetPasswordGuid = resetPasswordGuid;
    }

    public Date getResetPasswordSentDate() {
        return resetPasswordSentDate;
    }

    public void setResetPasswordSentDate(Date resetPasswordSentDate) {
        this.resetPasswordSentDate = resetPasswordSentDate;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


}
