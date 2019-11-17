package fxplatform.userservice.mail;

import java.io.InputStream;

public interface EmailProvider {
    void sendEmail(final String to,
                   final String subject,
                   final String text,
                   final String attachmentName,
                   final InputStream attachmentData);
}
