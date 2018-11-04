package at.ac.univie.dse.resources.dto;

import at.ac.univie.dse.auth.TokenHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jose4j.lang.JoseException;

import java.security.Principal;

/**
 * Wrapper class to ensure that returning a token always leads to the same response format
 */
public class TokenResponse {

    private static TokenHandler tokenHandler = new TokenHandler();

    public TokenResponse() {
        this.token = "null";
    }

    /**
     * Generate a token for a principal.
     * In our context those principals are usually of User.class
     *
     * @param user the principal
     * @throws JoseException
     * @see at.ac.univie.dse.core.User
     */
    public TokenResponse(Principal user) throws JoseException {
        this.token = tokenHandler.buildToken(user);
    }

    @JsonProperty("token")
    private String token;

}