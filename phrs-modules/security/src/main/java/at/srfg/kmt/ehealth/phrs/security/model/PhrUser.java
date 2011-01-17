/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package at.srfg.kmt.ehealth.phrs.security.model;


import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 * Generic representation for an user.
 *
 * @author Mihai
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "findUserForName", query = "SELECT u FROM PhrUser AS u WHERE u.name = :name"),
    @NamedQuery(name = "findUserForNameAndFamilyName", query = "SELECT u FROM PhrUser AS u WHERE u.name = :name AND u.familyName = :familyName"),
    @NamedQuery(name = "findUserForURI", query = "SELECT u FROM PhrUser AS u WHERE u.uri = :uri"),
    @NamedQuery(name = "findUserForNamePattern", query = "SELECT u FROM PhrUser AS u WHERE u.name LIKE :namePattern"),
    @NamedQuery(name = "findUserForNameAndFamilyPattern", query = "SELECT u FROM PhrUser AS u WHERE u.familyName LIKE :familyNamePattern AND u.name LIKE :namePattern"),
    @NamedQuery(name = "findAnonymusUser", query = "SELECT u FROM PhrUser AS u WHERE u.isAnonymous = true"),
    @NamedQuery(name = "removeAllUsers", query = "DELETE FROM PhrUser AS u"),
    @NamedQuery(name = "removeUserForName", query = "DELETE FROM PhrUser AS u WHERE u.name = :name"),
    @NamedQuery(name = "getAllUsers", query = "SELECT u FROM PhrUser AS u")
})
public class PhrUser implements Serializable {

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
     * The user name.
     */
    private String name;

    /**
     * The user family name.
     */
    private String familyName;

    /**
     * The unique general accepted user identifier.
     */
    private String uri;

    /**
     * Extra user information in any possible serializable format.
     */
    private Serializable original;
    
    
    private boolean isAnonymous;
    
    
    private Set<PhrGroup> phrGroups;
    
    private Set<PhrRole> phrRoles;

    /**
     * Builds an <code>User</code> instance.
     */
    public PhrUser() {
        // UNIMPLEMEMENTED
    }

    /**
     * Builds an <code>User</code> instance for a given name and family name.
     *
     * @param name the person name.
     * @param familyName the family name.
     */
    public PhrUser(String name, String familyName) {
        this.name = name;
        this.familyName = familyName;
    }

    /**
     * Returns the family name for this user.
     * 
     * @return the family name for this user.
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Returns the unique id for this User instance. <br>
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
     * Returns the name for this user.
     *
     * @return the name for this user.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the extra information for this user in serializable format.
     *
     * @return the extra information for this user.
     */
    @Lob 
    @Basic(fetch=FetchType.LAZY)
    public Serializable getOriginal() {
        return original;
    }

    /**
     * Returns the unique general accepted user identifier.
     *
     * @return  the unique general accepted user identifier.
     */
    public String getUri() {
        return uri;
    }

    /**
     * Registers a new family name for this user.
     *
     * @param familyName the new family name for this user.
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Registers a new unique id for this User instance.<br>
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
     * Registers a new name for this User instance.
     *
     * @param name the new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Registers a new extra user information for this User instance.
     * The extra user information can be everything
     * (e.g. xml documents, JSON objects, etc).
     *
     * @param name the extra user information.
     */
    public void setOriginal(Serializable original) {
        this.original = original;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    @ManyToMany
    public Set<PhrGroup> getPhrGroups() {
        return phrGroups;
    }

    public void setPhrGroups(Set<PhrGroup> phrGroups) {
        this.phrGroups = phrGroups;
    }

    @ManyToMany
    public Set<PhrRole> getPhrRoles() {
        return phrRoles;
    }

    
    public void setPhrRoles(Set<PhrRole> phrRoles) {
        this.phrRoles = phrRoles;
    }
    
    /**
     * Returns a string representation for this User.
     * The string representation for this User looks like
     * this :
     * <ul>
     * <li> the String 'User{name='
     * <li> the name property
     * <li> the String ', familyName='
     * <li> the familyName property
     * <li> the String ', uri='
     * <li> the uri property
     * <li> the String '}'
     * </ul>
     *
     * @return a string representation for this Group.
     */
    @Override
    public String toString() {
        final String result =
                String.format("User{name=%s, familyName=%s,uri=%s}", name, familyName, uri);

        return result;
    }
}
