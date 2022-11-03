package by.cinderella.model.user;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = CascadeType.ALL,optional=true)
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
        /*Comparator<UserOrganizer> comparator = Comparator.comparing(UserOrganizer :: getOrganizerGroup, Comparator.nullsFirst(Comparator.naturalOrder()));
        comparator = comparator.thenComparing(Comparator.comparing(organizer -> organizer.getOrganizer().getPrice()));*/

//        Comparator<UserOrganizer> comparator = (a, b) -> {
//            int result;
//            if (a.getOrganizerGroup() == null) {
//                result = (b.getOrganizerGroup() == null) ? 0 : -1;
//            } else if (b.getOrganizerGroup() == null){
//                result = 1;
//            } else {
//                result = a.getOrganizerGroup().compareToIgnoreCase(b.getOrganizerGroup());
//            }
//
//            /*if (result == 0) {
//                if (a.getOrganizer().getPrice() == null) {
//                    return 0;
//                }
//                if (b.getOrganizer().getPrice() == null) {
//                    return 1;
//                }
//                result = a.getOrganizer().getPrice().compareTo(b.getOrganizer().getPrice());
//            }*/
//            return result;
//        };
//        Comparator newComparator = Comparator.comparing(UserOrganizer::getOrganizerGroup,
//                Comparator.nullsFirst(Comparator.naturalOrder())
//        );
//
//        return (Set<UserOrganizer>) userOrganizerList.stream()
//                .sorted(newComparator)
//                .collect(Collectors.toSet());
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
