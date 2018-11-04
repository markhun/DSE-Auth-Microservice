package at.ac.univie.dse.auth;

import at.ac.univie.dse.core.User;
import at.ac.univie.dse.db.AbstractUserDAO;
import at.ac.univie.dse.db.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This authenticator handles the Bearer-Token requests
 */
public class JWTAuthenticator implements Authenticator<JwtContext, User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticator.class);
    private static AbstractUserDAO db = UserDAO.getInstance();

    JWTAuthenticator(UserDAO dao) {
        db = dao;
    }

    /**
     *
     * @param ctx JwtContext provided by ms1Application.class
     * @return Empty if credentials are invalid
     */
    @Override
    @SuppressWarnings("unchecked")
    @UnitOfWork
    public Optional<User> authenticate(JwtContext ctx) throws AuthenticationException {
        // if expiration time is NOT handled by the JWTconsumer it has to be handled here manually !!!

        try {
            JwtClaims claims = ctx.getJwtClaims();

            // parse the claims
            // the subject field should hold the UserID
            int id = Integer.parseInt(claims.getSubject());
            String username = (String) claims.getClaimValue("username");
            Set<String> privileges = new HashSet<>( (ArrayList<String>) claims.getClaimValue("privileges") );

            Optional<User> u = db.findByName(username);

            if( u.isPresent()) {
                User user = u.get();

                // check token for manipulations or inconsistencies
                if( ! privileges.equals(user.getPrivileges()) ) {
                    LOGGER.debug("inconsistent privileges for user: {}", user.getName());
                    return Optional.empty();
                }

                // Everything seems fine so we can authenticate the user
                LOGGER.debug("successfully authenticated user: {}", user.getName());
                return u;
            } else {
                LOGGER.debug("user: {} wasn't found in DB", username);
                return Optional.empty();
            }


        } catch (ClassCastException e) {
            LOGGER.error("Failed to authorise user due to malformed privileges claim: {}", e.getMessage(), e);
            return Optional.empty();
        } catch (MalformedClaimException e) {
            LOGGER.error("Failed to authorise user due to malformed claims: {}", e.getMessage(), e);
            return Optional.empty();
        } catch (HibernateException e) {
            LOGGER.debug("Unable to validate credentials.", e);
            throw new AuthenticationException("database unavailable");
        }

    }
}