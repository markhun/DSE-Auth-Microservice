package at.ac.univie.dse.db;

import at.ac.univie.dse.core.SignUpUser;
import at.ac.univie.dse.core.User;
import at.ac.univie.dse.ms1Application;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A DAO to manage User.class instances with a database
 *
 * @see User
 */
public class UserDAO extends AbstractDAO<User> implements AbstractUserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
    private static UserDAO instance;

    /**
     *
     * @param sessionFactory the SessionFactory for the database connection
     */
    private UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * This UserDAO is implemented as a singleton
     * TODO: this isn't best practice when working with dropwizard and should be changed
     * @return application wide instance of UserDAO
     */
    public static synchronized UserDAO getInstance() {
        if (UserDAO.instance == null) {
            UserDAO.instance = new UserDAO(ms1Application.hibernate.getSessionFactory());
        }
        return UserDAO.instance;
    }

    /**
     * add a new user to the database based on a SignUpUser Instance
     *
     * @param newUser the SignUpUser Instance to use.
     * @return true if successful
     * @throws IllegalCredentialsException if username is already in use
     * @see SignUpUser
     */
    @Override
    public boolean addNewUser(SignUpUser newUser) throws IllegalCredentialsException {
        Optional<User> o = findByName(newUser.getUsername());
        if( o.isPresent() ) throw new IllegalCredentialsException("Username already in use!");

        String username = newUser.getUsername();
        String password = newUser.getPassword();
        String email = newUser.getEmail();

        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setEmail(email);
        u.setPrivileges(Collections.emptySet());

        persist(u);

        return true;
    }

    /**
     * @deprecated
     * @param username
     * @param password
     * @param email
     * @return true if successful
     * @throws IllegalCredentialsException if username is already in use
     */
    @Override
    public boolean addNewUser(String username, String password, String email) throws IllegalCredentialsException {
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setEmail(email);
        u.setPrivileges(Collections.emptySet());

        persist(u);
        return true;
    }

    /**
     *
     * @param u
     * @return true if successful
     */
    @Override
    public boolean deleteUser(User u) {
    try{
        currentSession().delete(u);
        return true;
    } catch (HibernateException e) {
        LOGGER.debug("Unable to delete user {}", u.getName(), e);
        return false;
    }
    }

    /**
     * updates a user from
     *
     * @param updatedUser
     * @return true if successful
     */
    @Override
    public boolean updateUser(User updatedUser) {
        try {
            currentSession().update(updatedUser);
            return true;
        } catch (HibernateException e) {
            LOGGER.debug("Unable to update user {}",updatedUser.getName(), e);
            return false;
        }
    }


    /**
     *
     * @param id
     * @return
     */
    @Override
    public Optional<User> findByID(Integer id) {
        return Optional.ofNullable( currentSession().get(User.class, id) );
    }

    @Override
    public Optional<User> findByName(String login) {
        User user = currentSession().byNaturalId( at.ac.univie.dse.core.User.class )
                .using("username",login)
                .load();
        return Optional.ofNullable(user);
    }

    /**
     *
     * @return List of all User objects stored in database
     */
    @Override
    public List<User> findAll() {
        CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        Query<User> query = currentSession().createQuery(criteriaQuery);
        List<User> result = query.getResultList();
        return result;
    }

}
