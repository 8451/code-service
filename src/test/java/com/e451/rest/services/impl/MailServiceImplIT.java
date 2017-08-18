package com.e451.rest.services.impl;

import com.e451.rest.RestApplication;
import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.services.MailService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by j747951 on 6/20/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MailServiceImplIT {

    @SuppressWarnings("CanBeFinal")
    @Autowired
    private MailService mailService;

    @Ignore
    @Test
    public void testEmail() {
        DirectEmailMessage message = new DirectEmailMessage(new String[] {"jake.zarobsky@8451.com"},
                1, "Test", "test");
        mailService.sendEmail(message);
    }
}
