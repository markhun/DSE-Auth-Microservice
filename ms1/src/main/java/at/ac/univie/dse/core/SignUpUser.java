package at.ac.univie.dse.core;

import at.ac.univie.dse.db.AbstractUserDAO;
import at.ac.univie.dse.db.UserDAO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * This class is used to signup new users.
 *
 * This means instances of User.class should be constructed from instances of SignUpUser.class
 * additional credential validation can be handled here.
 *
 * @see User
 */
public class SignUpUser {

    private static final AbstractUserDAO db = UserDAO.getInstance();

    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    @NotEmpty
    // TODO: (secure) password handling
    private String password;

    private SignUpUser() {

    }


    @JsonCreator
    public SignUpUser(@JsonProperty("email") final String email,
                      @JsonProperty("username") final String username,
                      @JsonProperty("password") final String password) {
        this.email = email;
        this.username = username;
        this.password = password;

    }



    public String getUsername() {
        return username;
    }

    private void setUsername(String username)  {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    private void setPassword(String password) {
        this.password = password;
    }

    /**
     * verify email address
     *
     * https://javaee.github.io/javamail/
     *
     * @deprecated replaced with bean validation via hibernate
     * @param email
     * @return true if valid email format, false otherwise
     */
    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAdd = new InternetAddress(email);
            emailAdd.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }


}
