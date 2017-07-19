package com.e451.rest.services.impl;

import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by j747951 on 6/20/2017.
 */
@Service
@Profile("!prod")
public class MailServiceImpl implements MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

    private final JavaMailSender mailSender;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${code.mail.from}")
    private String fromMailAddress;

    private String getFromMailAddress() {
        if(fromMailAddress == null) {
            fromMailAddress = "code@8451.com";
        }

        return fromMailAddress;
    }

    @Value("${code.mail.alias}")
    private String fromAlias;

    private String getFromAlias() {
        if(fromAlias == null) {
            fromAlias = "Code";
        }

        return fromAlias;
    }

    @Override
    public void sendEmail(DirectEmailMessage directEmailMessage) {
        Assert.notNull(directEmailMessage, "Cannot send a null email message");
        Assert.notNull(directEmailMessage.getTo(), "Must have at least one recipient");
        Assert.notNull(directEmailMessage.getSubject(), "Must have a subject");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(mimeMessage, false);
            helper.setFrom(getFromMailAddress(), getFromAlias());
            helper.setTo(directEmailMessage.getTo());
            helper.setSubject(directEmailMessage.getSubject());
            helper.setText(directEmailMessage.getBody(), directEmailMessage.isHtml());
            helper.setPriority(directEmailMessage.getPriority());

            mailSender.send(mimeMessage);

        } catch (MessagingException | UnsupportedEncodingException | MailException ex) {
            LOG.error("Exception while attempting to send email", ex);
        }
    }
}
