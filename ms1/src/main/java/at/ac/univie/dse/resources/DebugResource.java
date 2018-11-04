package at.ac.univie.dse.resources;

import at.ac.univie.dse.core.SignUpUser;
import at.ac.univie.dse.core.User;
import at.ac.univie.dse.db.AbstractUserDAO;
import at.ac.univie.dse.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/debug")
public class DebugResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugResource.class);
    private static final AbstractUserDAO db = UserDAO.getInstance();

    @GET
    @Path("/verifyToken")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public User verifyToken(@Auth User user) {
        LOGGER.debug("generating /verifyToken response for User {}", user.getName());
        return user;
    }

    @GET
    @Path("/signUpExample")
    @Produces(MediaType.APPLICATION_JSON)
    public SignUpUser getSignUpExampleObject(@Auth User user) {
        LOGGER.debug("generating /signUpExample response for User {}", user.getName());
        SignUpUser signUpUser = new SignUpUser(user.getEmail(), user.getUsername(), user.getPassword());
        return signUpUser;
    }

    @GET
    @Path("/allUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public List<User> getAllUsers() {
        return db.findAll();
    }
}
