package by.cinderella.model.organizer;

import by.cinderella.model.Role;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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


    @ElementCollection(targetClass = OrganizerCategory.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "organizer_category", joinColumns = @JoinColumn(name = "organizer_id"))
    @Enumerated(EnumType.STRING)
    private Set<OrganizerCategory> categories = new HashSet<>();

    private Seller seller;

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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<OrganizerCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<OrganizerCategory> categories) {
        this.categories = categories;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
