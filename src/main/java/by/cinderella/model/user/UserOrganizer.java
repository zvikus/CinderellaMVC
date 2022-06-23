package by.cinderella.model.user;

import by.cinderella.model.organizer.Organizer;

import javax.persistence.*;

@Entity
@Table(name = "o_list_organizer")
public class UserOrganizer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int count;
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private Organizer organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ol_id")
    private OrganizerList organizerList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public OrganizerList getOrganizerList() {
        return organizerList;
    }

    public void setOrganizerList(OrganizerList organizerList) {
        this.organizerList = organizerList;
    }
}
