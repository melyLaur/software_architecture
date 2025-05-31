package fr.esgi.api.model.employee.email;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Value object representing a validated email address.
 */
public class Email {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final String value;

    /**
     * Private constructor to enforce use of factory method.
     *
     * @param value the validated email string
     */
    private Email(String value) {
        this.value = value;
    }

    /**
     * Check if the given email string matches the required pattern.
     *
     * @param email the email string to validate
     * @return true if valid, false otherwise
     */
    private static boolean validate(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    /**
     * Factory method to create an Email instance after validating format.
     *
     * @param value the email string
     * @return a new Email object
     * @throws WrongEmailFormatException if the format is invalid
     */
    public static Email of(String value) {
        if (!validate(value)) {
            throw new WrongEmailFormatException();
        }
        return new Email(value);
    }

    /**
     * Retrieve the underlying email string.
     *
     * @return the email value
     */
    public String getValue() {
        return value;
    }
}