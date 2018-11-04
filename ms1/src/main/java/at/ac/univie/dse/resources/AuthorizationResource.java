package at.ac.univie.dse.resources;

import at.ac.univie.dse.core.User;
import at.ac.univie.dse.db.AbstractUserDAO;
import at.ac.univie.dse.db.UserDAO;
import at.ac.univie.dse.resources.dto.TokenResponse;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Set;

@Path("/authorization")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorizationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationResource.class);
    private static final AbstractUserDAO db = UserDAO.getInstance();

    public AuthorizationResource() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Set<String> getPriviliges(@Auth User user) throws JoseException {
        LOGGER.debug("returning GET /authorization for User {}", user.getName());
        return user.getPrivileges();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response addPrivileges(@Auth User user, ArrayList<String> newPriviliges) {
        LOGGER.debug("returning PUT /authorization for User {}", user.getName());

        for (String s : newPriviliges)
            user.addPrivilege(s);

        boolean success = db.updateUser(user);
        if(!success) return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();

        try {
            LOGGER.debug("successfully returned new Token in PUT /authorization for User {}", user.getName());
            return Response.ok(new TokenResponse(user)).build();
        } catch (JoseException e) {
            LOGGER.error("TokenHandler was unable to provide a token: {}", e);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response removePrivileges(@Auth User user, ArrayList<String> PriviligesToRemove) {
        LOGGER.debug("returning DELETE /authorization for User {}", user.getName());
        // combining new and old privileges
        for (String s : PriviligesToRemove)
            user.removePrivilige(s);

        boolean success = db.updateUser(user);
        if(!success) return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();

        try {
            LOGGER.debug("successfully returned new Token in DELETE /authorization for User {}", user.getName());
            return Response.ok(new TokenResponse(user)).build();
        } catch (JoseException e) {
            LOGGER.error("TokenHandler was unable to provide a token: {}", e);
            return Response.serverError().build();
        }
    }

}