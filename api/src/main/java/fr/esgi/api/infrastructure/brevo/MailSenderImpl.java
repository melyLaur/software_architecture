package fr.esgi.api.infrastructure.brevo;

import fr.esgi.api.model.reservation.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailSenderImpl implements MailSender {
    private static final Logger logger = LoggerFactory.getLogger(MailSenderImpl.class);

    private static final String FROM = "doctodoc.cestmieuxquedoctolib@gmail.com";

    @Override
    public void sendMail(String to, String subject, String body) {
        System.out.println("send email");
    }
}
