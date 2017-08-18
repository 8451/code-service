package com.e451.rest.services.impl;

import com.e451.rest.domains.email.DirectEmailMessage;
import com.e451.rest.services.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by j747951 on 6/20/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MailServiceImplTest {
    @SuppressWarnings("CanBeFinal")
    @Mock
    private JavaMailSender javaMailSender;

    @SuppressWarnings("CanBeFinal")
    @InjectMocks
    private MailServiceImpl mailService;

    private DirectEmailMessage getDirectEmailMessage() {
        return new DirectEmailMessage(new String[] {"toAddress"}, 1, "subject", "body");
    }

    @Test
    public void whenSendingEmail_thenMailSenderIsCalled() throws Exception {
        DirectEmailMessage directEmailMessage = getDirectEmailMessage();
        MimeMessage mockMimeMessage = mock(MimeMessage.class);

        doReturn(mockMimeMessage).when(javaMailSender).createMimeMessage();
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        mailService.sendEmail(directEmailMessage);

        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    public void whenSendingEmail_throwsException_exceptionSwallowed() {
        DirectEmailMessage directEmailMessage = getDirectEmailMessage();
        MimeMessage mockMimeMessage = mock(MimeMessage.class);

        doReturn(mockMimeMessage).when(javaMailSender).createMimeMessage();
        doThrow(new MailAuthenticationException("error")).when(javaMailSender).send(any(MimeMessage.class));

        mailService.sendEmail(directEmailMessage);
    }
}
