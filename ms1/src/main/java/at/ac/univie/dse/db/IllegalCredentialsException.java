package at.ac.univie.dse.db;

/**
 * This exception should be thrown if Credentials provided for a new User are not valid.
 * e.g. a username is already in use.
 */
public class IllegalCredentialsException extends Exception {

    public  IllegalCredentialsException() {}

    IllegalCredentialsException(String message)
    {
        super(message);
    }

}
