package by.cinderella.model.organizer;

import by.cinderella.model.Role;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer length;
    private Integer width;
    private Integer height;

    private Integer price;

    private String link;


    private Date lastUpdated;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "organizer_category", joinColumns = @JoinColumn(name = "organizer_id"))
    @Enumerated(EnumType.STRING)
    private Set<OrganizerCategory> categories;

    private Seller seller;



}
