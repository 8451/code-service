package com.e451.rest.domains.email;

import com.e451.rest.domains.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by j747951 on 6/20/2017.
 */
public class RegistrationEmailMessage extends DirectEmailMessage {

    private User user;
    private String codeWebAddress;

    public RegistrationEmailMessage(User user, String codeWebAddress) {
        this.user = user;
        this.codeWebAddress = codeWebAddress;
        super.setHtml(false);
        super.setPriority(1);
        super.setTo(new String[] {user.getEmail()});
        super.setSubject("Activate your account");
    }

    @Override
    public String getBody() {
        if(this.body == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("Hi " + user.getFirstName() + "!\n\n");
            builder.append("We've created your account, just go to the activate page at the link below:\n");
            builder.append(String.format("%s/activate/%s\n", codeWebAddress, user.getActivationGuid().toString()));
            builder.append(String.format("Or use the activation code: %s\n\n", user.getActivationGuid().toString()));
            builder.append("We hope you enjoy using Code!");
            this.body = builder.toString();
        }

        return this.body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.body = null; // force regeneration when text is done.
    }

    public String getCodeWebAddress() {
        return codeWebAddress;
    }

    public void setCodeWebAddress(String codeWebAddress) {
        this.codeWebAddress = codeWebAddress;
    }
}
