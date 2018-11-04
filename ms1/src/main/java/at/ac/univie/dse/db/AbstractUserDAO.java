package at.ac.univie.dse.db;

import at.ac.univie.dse.core.SignUpUser;
import at.ac.univie.dse.core.User;

import java.util.List;
import java.util.Optional;

/**
 * This interface was used to prototype the data access functions
 */
public interface AbstractUserDAO {

    boolean addNewUser(SignUpUser newUser) throws IllegalCredentialsException;

    boolean addNewUser(String login, String password, String email) throws IllegalCredentialsException;

    boolean deleteUser(User u);

    boolean updateUser(User updatedUser);

    Optional<User> findByID(Integer id);

    Optional<User> findByName(String login);

    List<User> findAll();

}
