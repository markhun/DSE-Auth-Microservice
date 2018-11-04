package at.ac.univie.dse.resources;

import at.ac.univie.dse.core.User;
import at.ac.univie.dse.resources.dto.TokenResponse;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.hibernate.UnitOfWork;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authentication")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);

    public AuthenticationResource() {

    }

    @GET
    @UnitOfWork
    public Response login(@Auth PrincipalImpl user) {
        try {

            return Response.ok( new TokenResponse(user) ).build();

        } catch (IllegalStateException e) {
            LOGGER.error("Server in illegal state! ", e);
            return Response.serverError().entity("Server Error: " + e.getMessage()).build();
        } catch (JoseException e) {
            LOGGER.error("TokenHandler was unable to provide a token: {}", e);
            return Response.serverError().build();
        }
    }

    @GET
    @UnitOfWork
    @Path("/refresh")
    public Response refresh(@Auth User user) {
        try {

            return Response.ok( new TokenResponse(user) ).build();

        } catch (IllegalStateException e) {
            LOGGER.error("Server in illegal state! ", e);
            return Response.serverError().entity("Server Error: " + e.getMessage()).build();
        } catch (JoseException e) {
            LOGGER.error("TokenHandler was unable to provide a token: {}", e);
            return Response.serverError().build();
        }
    }
}