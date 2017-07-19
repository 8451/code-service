package com.e451.rest.services.impl;

import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.services.MailService;
import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by j747951 on 7/19/2017.
 */
@Service
@Profile("prod")
public class SendGridMailServiceImpl implements MailService {

    private static final Logger LOG = LoggerFactory.getLogger(SendGridMailServiceImpl.class);

    private final SendGrid sendGrid;
    private final String fromEmail;
    private final String alias;

    @Autowired
    public SendGridMailServiceImpl(@Value("${code.mail.apiKey}") String apiKey,
                                   @Value("${code.mail.from}") String fromEmail,
                                   @Value("${code.mail.alias}") String alias) {
        sendGrid = new SendGrid(apiKey);
        this.alias = alias;
        this.fromEmail = fromEmail;
    }

    @Override
    public void sendEmail(DirectEmailMessage directEmailMessage) {
        Email from = new Email(fromEmail, alias);

        Content content = new Content(directEmailMessage.isHtml() ? "text/html" : "text/plain",
                directEmailMessage.getBody());

        for(String to: directEmailMessage.getTo()) {
            executeRequest(new Mail(from, directEmailMessage.getSubject(), new Email(to), content));
        }
    }

    private Response executeRequest(Mail mail) {
        Request request = new Request();
        Response response = null;
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response = sendGrid.api(request);
        } catch (Exception ex) {
            LOG.error("Encountered exception sending email", ex);
        }

        return response;
    }
}
