package fr.esgi.api.model.reservation;

import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class ReservationMailService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final MailSender mailSender;

    public ReservationMailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRecap(Employee employee, Reservation reservation, boolean electrical) {
        String subject = "Confirmation de votre réservation de place";
        String type = electrical ? "ÉLECTRIQUE" : "STANDARD";
        String body;

        if (employee.getRole() == EmployeeRole.MANAGER) {
            body = managerBody(employee, reservation, type);
        } else {
            body = employeeBody(employee, reservation, type);
        }

        mailSender.sendMail(employee.getEmail().getValue(), subject, body);
    }

    private String managerBody(Employee employee, Reservation reservation, String type) {
        return String.format("""
                Bonjour %s %s,

                Votre réservation MANAGER a bien été enregistrée :
                  • Du    : %s
                  • Au    : %s
                  • Place : %s
                  • Type  : %s

                Ce QR-code / e-mail est valable chaque jour jusqu’à 11 h.
                Après cette heure, la place peut être réaffectée.

                — Park Slot
                """,
                employee.getFirstName(),
                employee.getLastName(),
                reservation.getStartDate().format(DATE_FMT),
                reservation.getEndDate().format(DATE_FMT),
                reservation.getPlace().getIdentifier(),
                type
        );
    }

    private String employeeBody(Employee employee, Reservation reservation, String type) {
        return String.format("""
                Bonjour %s %s,

                Votre réservation a bien été enregistrée :
                  • Date  : %s
                  • Place : %s
                  • Type  : %s

                Ce QR-code est valable jusqu’à 11 h le jour de la réservation.
                Au-delà, la place redevient disponible.

                — Park Slot
                """,
                employee.getFirstName(),
                employee.getLastName(),
                reservation.getStartDate().format(DATE_FMT),
                reservation.getPlace().getIdentifier(),
                type
        );
    }
}