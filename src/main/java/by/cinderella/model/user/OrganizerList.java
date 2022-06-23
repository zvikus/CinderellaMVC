package by.cinderella.model.user;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizer_list")
public class OrganizerList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Date created;
    private Date lastUpdated;

    /*@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "o_list_organizer",
            joinColumns = { @JoinColumn(name = "ol_id") },
            inverseJoinColumns = { @JoinColumn(name = "organizer_id") })
    private Set<Organizer> organizerList = new HashSet<>();*/

    @OneToMany(mappedBy="organizerList", fetch=FetchType.EAGER)
    private Set<UserOrganizer> userOrganizerList = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY,optional=true)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<UserOrganizer> getUserOrganizerList() {
        return userOrganizerList;
    }

    public void setUserOrganizerList(Set<UserOrganizer> userOrganizerList) {
        this.userOrganizerList = userOrganizerList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
