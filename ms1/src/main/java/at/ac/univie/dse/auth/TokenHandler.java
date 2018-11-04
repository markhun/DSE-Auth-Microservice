package at.ac.univie.dse.auth;

import at.ac.univie.dse.core.User;
import at.ac.univie.dse.db.AbstractUserDAO;
import at.ac.univie.dse.db.UserDAO;
import at.ac.univie.dse.ms1Application;
import io.dropwizard.hibernate.UnitOfWork;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.Optional;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

// TODO: given time, replace this with a proper factory!

/**
 * This is the TokenHandler based on the tokenType property in config.yml it will just sign the JWTokens or
 * sign and encrypt them
 */
public class TokenHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenHandler.class);
    private static final AbstractUserDAO db = UserDAO.getInstance();

    /**
     *
     *
     * @param user
     * @return
     * @throws JoseException
     * @throws IllegalStateException if a user account was found in the database during authentication but not now
     */
    public String buildToken(Principal user) throws JoseException, IllegalStateException {
        String tokenType = ms1Application.tokenType;

        try {

            switch (tokenType) {
                case "jws":
                    LOGGER.debug("building token of type jws");
                    return buildJWS(user);
                case "jwe":
                    LOGGER.debug("building token of type jwe");
                    return buildJWE(user);
                default:
                    throw new IllegalArgumentException("no valid tokentype provided!");
            }
        } catch (JoseException e) {
            LOGGER.error("Unable to generate tokens: {} ", e);
            throw e;
        }
    }

    /**
     * building a signed JWT
     *
     * @param user
     * @return
     * @throws JoseException
     * @throws IllegalStateException if a user account was found in the database during authentication but not now
     */
    @UnitOfWork
    private String buildJWS(Principal user) throws JoseException, IllegalStateException {
        Optional<User> u = db.findByName( user.getName() );
        if ( !u.isPresent() ) throw new IllegalStateException("User Account found during authentication, but not during token creation!");

        Integer id = u.get().getID();

        final JwtClaims claims = new JwtClaims();
        claims.setSubject(id.toString()); // the subject/principal is whom the token is about
        claims.setClaim("privileges", u.get().getPrivileges() );
        claims.setStringClaim("username", user.getName());
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture(45);
        claims.setGeneratedJwtId();

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(ms1Application.key);
        return jws.getCompactSerialization();
    }

    /**
     * building a signed JWT and then encrypting it in a JWE
     *
     * @param user
     * @return
     * @throws JoseException
     * @throws IllegalStateException
     */
    private String buildJWE(Principal user) throws JoseException, IllegalStateException {
        String innerJWT = buildJWS(user);

        final JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setContentTypeHeaderValue("JWT");
        jwe.setPayload(innerJWT);
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT); // Direct encryption with a shared symmetric key
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(ms1Application.key);
        return jwe.getCompactSerialization();
    }
}
