package at.ac.univie.dse.auth;

import at.ac.univie.dse.core.User;
import at.ac.univie.dse.db.AbstractUserDAO;
import at.ac.univie.dse.db.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Our microservice architecture needs a point of entry: A way to login a User and obtain a Bearer-Token to use the
 * other services in our todolist application
 * Currently this is done via "Basic Access Authentication"
 * see RFC 7235 or https://en.wikipedia.org/wiki/Basic_access_authentication
 *
 * TODO: "Basic Access Authentication" is insecure!
 * Ideally ms3 should generate a (encrypted) Login-Token with the claims Username and Password and exchange this token
 * at this microservice (ms1) with a Bearer-Token. For this ms3 should use it's own private key to sign (and encrypt) the
 * Login-Token, so Login-Tokens can't be confused with Bearer-Tokens.
 * This would ensure complete encrypted data transfer even when using http
 */
public class BasicAuthenticator implements Authenticator<BasicCredentials, PrincipalImpl> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticator.class);
    private static AbstractUserDAO db = UserDAO.getInstance();

    BasicAuthenticator(UserDAO dao) {
        db = dao;
    }

    /**
     *
     * @param credentials
     * @return Empty if credentials are invalid
     * @throws AuthenticationException when Authenticator is UNABLE to check the validity of the given credentials
     */
    @Override
    @UnitOfWork
    public Optional<PrincipalImpl> authenticate(BasicCredentials credentials) throws AuthenticationException {
        Set<UUID> privileges = new HashSet<>();

        if (isValidCredentials(credentials)) {
            return Optional.of(new PrincipalImpl(credentials.getUsername()));
        }
        return Optional.empty();
    }

    @UnitOfWork
    private boolean isValidCredentials(BasicCredentials credentials) throws AuthenticationException {
        try {
            Optional<User> u = db.findByName(credentials.getUsername());

            if (u.isPresent() && u.get().getPassword().equals(credentials.getPassword()) ) {
                LOGGER.debug("successfully authenticated user: {}", credentials.getUsername());
                return true;
            }
            else {
                LOGGER.debug("Failed to authenticate user: {}", credentials.getUsername());
                return false;
            }

        } catch (HibernateException e) {
            LOGGER.debug("Unable to validate credentials for username {}.", credentials.getUsername(), e);
            throw new AuthenticationException("database unavailable");
        }

    }
}
