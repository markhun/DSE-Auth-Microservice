package at.ac.univie.dse.db;

import at.ac.univie.dse.core.SignUpUser;
import at.ac.univie.dse.core.User;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This DAO based on BiMap was used during development
 *
 * @deprecated
 */
public class mapUserDAO implements AbstractUserDAO{

    private static mapUserDAO instance;
    private BiMap<Integer, User> db = HashBiMap.create();
    private static AtomicInteger idCounter = new AtomicInteger();

    private mapUserDAO() {
        // saving some example accounts
        Set<String> priv1 = new HashSet<>();
        priv1.add( UUID.randomUUID().toString() );
        priv1.add( UUID.randomUUID().toString() );
        priv1.add( UUID.randomUUID().toString() );

        Set<String> priv2 = new HashSet<>();
        priv2.add( UUID.randomUUID().toString() );
        priv2.add( UUID.randomUUID().toString() );

        Set<String> priv3 = new HashSet<>();
        priv3.add( UUID.randomUUID().toString() );
        priv3.add( UUID.randomUUID().toString() );
        priv3.add( UUID.randomUUID().toString() );
        priv3.add( UUID.randomUUID().toString() );

        User testuser1 = new User();
        testuser1.setUsername("Testuser");
        testuser1.setPassword("testpw");
        testuser1.setEmail("test@example.com");
        testuser1.setPrivileges(priv1);

        User testuser2 = new User();
        testuser2.setUsername("Testuser2");
        testuser2.setPassword("testpw");
        testuser2.setEmail("test@example.com");
        testuser2.setPrivileges(priv2);

        User testuser3 = new User();
        testuser3.setUsername("Testuser3");
        testuser3.setPassword("testpw");
        testuser3.setEmail("test@example.com");
        testuser3.setPrivileges(priv3);

        testuser1.setId(idCounter.getAndIncrement());
        testuser2.setId(idCounter.getAndIncrement());
        testuser3.setId(idCounter.getAndIncrement());

        db.put(testuser1.getID(), testuser1);
        db.put(testuser2.getID(), testuser2);
        db.put(testuser3.getID(), testuser3);

    }

    public static synchronized mapUserDAO getInstance () {
        if (mapUserDAO.instance == null) {
            mapUserDAO.instance = new mapUserDAO();
        }
        return mapUserDAO.instance;
    }


    /**
     *
     * @param newUser
     * @throws IllegalCredentialsException
     * @return true if added successful
     */
    @Override
    public boolean addNewUser(SignUpUser newUser) throws IllegalCredentialsException {

            Optional<User> o = findByName(newUser.getUsername());
            if( o.isPresent() ) throw new IllegalCredentialsException("Username already in use!");

            int userId = idCounter.getAndIncrement();
            String username = newUser.getUsername();
            String password = newUser.getPassword();
            String email = newUser.getEmail();

            User u = new User();
            u.setId(userId);
            u.setUsername(username);
            u.setPassword(password);
            u.setEmail(email);
            u.setPrivileges(Collections.emptySet());

            db.put(userId,u);
            return true;

    }

    /**
     *
     * @deprecated
     * @param username
     * @param password
     * @param email
     * @throws IllegalCredentialsException
     * @return true if added successful
     */
    @Override
    public boolean addNewUser(String username, String password, String email) throws IllegalCredentialsException {

            Optional<User> o = findByName(username);
            if( o.isPresent() ) throw new IllegalCredentialsException("Username already in use!");

            int userId = idCounter.getAndIncrement();

            User u = new User();
            u.setId(userId);
            u.setUsername(username);
            u.setPassword(password);
            u.setEmail(email);
            u.setPrivileges(Collections.emptySet());

            db.put(userId,u);
            return true;

    }

    /**
     *
     * @param u User to delete
     * @return true if successful
     */
    @Override
    public boolean deleteUser(User u) {
        return db.inverse().remove(u) != null;
    }

    @Override
    public boolean updateUser(User updatedUser) {
        Optional<User> oldUser = findByID( updatedUser.getID() );
        if (oldUser.isPresent()) {
            if ( oldUser.get().getUsername().equals(updatedUser.getUsername()) ) {
                db.put(updatedUser.getID(), updatedUser);
                return true;
            } else return false;
        } else return false;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Optional<User> findByID(Integer id) {
        return (db.get(id) != null) ? Optional.of(db.get(id)) : Optional.empty();
    }

    /**
     *
     * @param username
     * @return
     */
    @Override
    public Optional<User> findByName(String username) {
        for (User entry : db.values() ) {
            if ( entry.getName().equals(username) ) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    /**
     *
     * @return all stored users
     */
    @Override
    public List<User> findAll() {
        return new ArrayList<>(db.values());
    }

}
