package by.cinderella.model.user;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "restriction")
public class Restriction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional=true)
    @JoinColumn(name="service_id", nullable=false)
    private Service service;

    private Date activationDate;

    private Date expirationDate;

    private String operationId;

    @ManyToOne(fetch = FetchType.LAZY,optional=true)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
