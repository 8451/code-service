package com.e451.rest.domains.email;

import com.e451.rest.domains.user.User;

/**
 * Created by j747951 on 7/26/2017.
 */
public class ForgotPasswordEmailMessage extends DirectEmailMessage {

    private final User user;
    private final String codeWebAddress;

    public ForgotPasswordEmailMessage(User user, String codeWebAddress) {
        this.user = user;
        this.codeWebAddress = codeWebAddress;

        super.setHtml(false);
        super.setPriority(1);
        super.setTo(new String[] {user.getUsername()});
        super.setSubject("Forgot password");
    }

    @Override
    public String getBody() {
        if(this.body == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("Hi " + user.getFirstName() + "!\n\n");
            builder.append("You can reset your password by using the link below:\n");
            builder.append(String.format("%s/forgot-password/%s\n\n", codeWebAddress, user.getResetPasswordGuid().toString()));
            builder.append("This link is only good for a few minutes.");
            this.body = builder.toString();
        }

        return this.body;
    }

    public User getUser() {
        return user;
    }

    public String getCodeWebAddress() {
        return codeWebAddress;
    }
}
