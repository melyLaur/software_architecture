package fr.esgi.api.model.reservation;

public interface MailSender {

    void sendMail(String to, String subject, String body);
}
