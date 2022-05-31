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
}
