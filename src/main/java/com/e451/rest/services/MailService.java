package com.e451.rest.services;

import com.e451.rest.domains.email.DirectEmailMessage;

/**
 * Created by j747951 on 6/20/2017.
 */
public interface MailService {
    void sendEmail(DirectEmailMessage directEmailMessage);
}
