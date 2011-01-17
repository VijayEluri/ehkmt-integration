/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package at.srfg.kmt.ehealth.phrs.security.model;


import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Generic representation for an user role.
 * A role is a form to assign responsibilities to an user
 * (and to enrich in this way its meaning without to alter
 * the original user information).
 * An user can have none, one or more roles.
 *
 * @author Mihai
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "findRoleForName", query = "SELECT r FROM PhrRole AS r WHERE r.name = :name"),
    @NamedQuery(name = "findRoleForNamePattern", query = "SELECT r FROM PhrRole AS r WHERE r.name LIKE :namePattern"),
    @NamedQuery(name = "removeAllRoles", query = "DELETE FROM PhrRole AS g"),
    @NamedQuery(name = "removeRoleForName", query = "DELETE FROM PhrRole AS r WHERE r.name = :name"),
    @NamedQuery(name = "getAllRoles", query = "SELECT r FROM PhrRole AS r")
})
public class PhrRole implements Serializable {

    /**
     * A version number for this class so that serialization
     * can occur without worrying about the underlying class
     * changing between serialization and deserialization.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * Unique id, is generated by the underlying persistence layer.<br>
     * <b>Note : <b> Don't use this property in any kind of logic,
     * this property is used only by the underly persistence layer.
     */
    private Long id;

    /**
     * The role name.
     */
    private String name;

    /**
     * The role description.
     */
    private String description;

    /**
     * All the user members in this with this role.
     */
    private Set<PhrUser> phrUsers;

    /**
     * Builds an <code>PhrRole</code>PhrRole instance.
     */
    public PhrRole() {
        // UNIMPLMEEMNTED
    }

    /**
     * Builds an <code>PhrRole</code>PhrRole instance for a given name.
     */
    public PhrRole(String name) {
        if (name == null) {
            throw new NullPointerException("The argument name can not be null.");
        }

        this.name = name;
    }

    /**
     * Returns the description for this Role.
     *
     * @return the description for this Role.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the unique id for this Role instance. <br>
     * <b>Note : <b> Don't use this property in any kind of logic,
     * this property is used only by the underly persistence layer.
     *
     * @return the unique id for this user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Returns the name for this Role.
     *
     * @return the name for this Role.
     */
    public String getName() {
        return name;
    }

    
    /**
     * Registers a new description for this Group instance.
     *
     *
     * @param description a new description for this Group instance.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Registers a new unique id for this Group instance.<br>
     * <b>Note : <b> Don't set this property by hand (using this
     * method) and don't use this property any kind of logic.
     * This property is used only by the underly persistence layer.
     *
     * @param id the new unique id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Registers a new name for this group.
     * @param name the new name for this group.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Replaces the already existent set of users (for this group)
     * with a new set of users.
     *
     * @param users the new set of users.
     * @see #addUsers(java.util.Set)
     * @see #addUser(at.srfg.kmt.ehealth.phrs.security.model.User)
     */
    public void setPhrUsers(Set<PhrUser> users) {
        this.phrUsers = users;
    }

    @ManyToMany(mappedBy = "phrRoles", cascade=CascadeType.ALL)
    public Set<PhrUser> getPhrUsers() {
        return phrUsers;
    }

    @Transient
    public boolean isRoleEmpty() {
        return phrUsers == null ? true : phrUsers.isEmpty();
    }

    /**
     * Returns a string representation for this Role.
     * The string representation for this Role looks like
     * this :
     * <ul>
     * <li> the String 'Role{name='
     * <li> the name property
     * <li> the String ', description='
     * <li> the description property
     * <li> the String '}'
     * </ul>
     *
     * @return a string representation for this Role.
     */
    @Override
    public String toString() {
        final String result =
                String.format("Role{name=%s, description=%s}", name, description);
        return result;
    }
}
