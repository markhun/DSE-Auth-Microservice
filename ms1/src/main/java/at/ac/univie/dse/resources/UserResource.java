package at.ac.univie.dse.resources;

import at.ac.univie.dse.core.SignUpUser;
import at.ac.univie.dse.core.User;
import at.ac.univie.dse.db.AbstractUserDAO;
import at.ac.univie.dse.db.IllegalCredentialsException;
import at.ac.univie.dse.db.UserDAO;
import at.ac.univie.dse.resources.dto.TokenResponse;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    private static final AbstractUserDAO db = UserDAO.getInstance();

    public UserResource() {

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response createUser(@Valid final SignUpUser user) {
        try {

            db.addNewUser(user);
            return Response.ok().build();

        } catch (IllegalCredentialsException e) {
            return Response.status(Response.Status.CONFLICT).entity("{"+e.getMessage()+"}").build();
        }
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateUser(@Auth User user, @Valid final SignUpUser newUserCredentials) {
        if( user.getUsername().equals( newUserCredentials.getUsername() ) ) {
            user.setEmail(newUserCredentials.getEmail());
            user.setPassword(newUserCredentials.getPassword());

            boolean success = db.updateUser(user);

            if(!success) return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
            else {
                LOGGER.debug("changed credentials of user: {}", user.getName() );

                try {
                    return Response.ok( new TokenResponse(user)).build();
                } catch (JoseException e) {
                    LOGGER.error("TokenHandler was unable to provide a token: {}", e);
                    return Response.serverError().build();
                }
            }


        } else return Response.status(Response.Status.BAD_REQUEST)
                .entity("Usernames are permanent and can't be changed!")
                .build();
    }

    @DELETE
    @UnitOfWork
    public Response deleteUser(@Auth User user) {
        boolean success = db.deleteUser(user);

        if(!success) return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        else return Response.ok().build();
    }
}