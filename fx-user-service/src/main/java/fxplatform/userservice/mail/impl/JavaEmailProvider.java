package fxplatform.userservice.mail.impl;

import fxplatform.userservice.exception.FailMailException;
import fxplatform.userservice.mail.EmailProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class JavaEmailProvider implements EmailProvider {

    private final JavaMailSender mailSender;

    @Autowired
    public JavaEmailProvider(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(final String to,
                          final String subject,
                          final String text,
                          final String attachmentName,
                          final InputStream attachmentData) {
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setContent(text, "text/html");

            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            final DataSource dataSource = new ByteArrayDataSource(attachmentData, "application/octet-stream");
            helper.addAttachment(attachmentName, dataSource);
            mailSender.send(mimeMessage);

            log.info(String.format("Email to %s has been sent", to));
        } catch (final MessagingException | IOException ex) {
            log.error("Failed to send a message", ex);
            throw new FailMailException("Failed to send a message", ex);
        }
    }
}
