package by.cinderella.model.user;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Double cost;

    private Double cost1;
    private Double cost3;
    private Double cost6;
    private Double cost12;

    private boolean published;

    @Column(length = Integer.MAX_VALUE)
    private String description;

    @Column(length = Integer.MAX_VALUE)
    private String product;

    @Column(length = Integer.MAX_VALUE)
    private String shortDescription;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<Restriction> restrictions;

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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Set<Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Set<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost1() {
        return cost1;
    }

    public void setCost1(Double cost1) {
        this.cost1 = cost1;
    }

    public Double getCost3() {
        return cost3;
    }

    public void setCost3(Double cost3) {
        this.cost3 = cost3;
    }

    public Double getCost6() {
        return cost6;
    }

    public void setCost6(Double cost6) {
        this.cost6 = cost6;
    }

    public Double getCost12() {
        return cost12;
    }

    public void setCost12(Double cost12) {
        this.cost12 = cost12;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Transient
    public boolean isSubscription() {
        return cost == null;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
