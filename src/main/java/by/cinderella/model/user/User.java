package by.cinderella.model.user;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private boolean active;

    private String email;
    private String activationCode;

    private Date registrationDate;
    private Date activationDate;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    /*
    https://stackoverflow.com/questions/14570025/how-to-hibernate-annotate-mapstring-setstring-or-mapstring-mapstring-st
    @ElementCollection(fetch=FetchType.EAGER)
    @JoinTable(name = "Attribute",
            joinColumns = @JoinColumn(name = "fkAccount"))
    @MapKeyColumn(name = "name")
    // next line is working for Map<String, String>, but not here!
    @Column(name = "value")
    prvate Map<String, Set<String>> attributes = new HashMap<String, Set<String>>();*/

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Restriction> restrictions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Set<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }
}