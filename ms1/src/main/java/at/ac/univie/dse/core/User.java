package at.ac.univie.dse.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;
import java.util.Set;


/**
 * Main Principal implementation of ms1
 *
 */
@Entity
@Table(name = "USER")
public class User implements Principal, Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_sequence", allocationSize=1)
    @Column(name = "ID", updatable = false, nullable = false)
    @NotNull
    private Integer id;

    @Column(name = "USERNAME", unique=true, length = 20, nullable = false)
    @NaturalId
    @NotNull
    private String username;

    @Column(name = "EMAIL", length = 60, nullable = false)
    @NotNull
    private String email;

    // TODO: (secure) password handling
    @Column(name = "PASSWORD", length = 30, nullable = false)
    @NotNull
    private String password;

    @Column(name = "PRIVILEGES")
    @ElementCollection(targetClass=String.class)
    private Set<String> privileges;

    public User() {

    }

    public User(String username, String email, String password, Set<String> priv) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.privileges = priv;

    }

    public void setId(Integer id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }

    public void addPrivilege(String s) {
        privileges.add(s);
    }

    public boolean removePrivilige(String s) {
        return privileges.remove(s);
    }

    @Override
    public String getName() {
        return username;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public Set<String> getPrivileges() {
        return privileges;
    }

    @JsonProperty
    public int getID() {
        return id;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    /**
     * Users are considered equal when they share the same usernames.
     * a username is considered to be unique
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    /**
     * Users are considered equal when they share the same usernames.
     * a username is considered to be unique
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
