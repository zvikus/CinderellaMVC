package by.cinderella.model.organizer;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Filter {
    private String nameLike;

    private Double lengthFrom;
    private Double widthFrom;
    private Double heightFrom;

    private Double lengthTo;
    private Double widthTo;
    private Double heightTo;

    private Double priceFrom;
    private Double priceTo;

    private Set<OrganizerCategory> categories;
    private Set<Seller> seller;
    private Set<Material> material;

    public Filter(String nameLike, Double lengthFrom, Double widthFrom, Double heightFrom, Double lengthTo, Double widthTo, Double heightTo, Double priceFrom, Double priceTo, Set<OrganizerCategory> categories, Set<Seller> seller, Set<Material> material) {
        this.nameLike = nameLike;
        this.lengthFrom = lengthFrom;
        this.widthFrom = widthFrom;
        this.heightFrom = heightFrom;
        this.lengthTo = lengthTo;
        this.widthTo = widthTo;
        this.heightTo = heightTo;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.categories = categories;
        this.seller = seller;
        this.material = material;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public Double getLengthFrom() {
        return lengthFrom;
    }

    public void setLengthFrom(Double lengthFrom) {
        this.lengthFrom = lengthFrom;
    }

    public Double getWidthFrom() {
        return widthFrom;
    }

    public void setWidthFrom(Double widthFrom) {
        this.widthFrom = widthFrom;
    }

    public Double getHeightFrom() {
        return heightFrom;
    }

    public void setHeightFrom(Double heightFrom) {
        this.heightFrom = heightFrom;
    }

    public Double getLengthTo() {
        return lengthTo;
    }

    public void setLengthTo(Double lengthTo) {
        this.lengthTo = lengthTo;
    }

    public Double getWidthTo() {
        return widthTo;
    }

    public void setWidthTo(Double widthTo) {
        this.widthTo = widthTo;
    }

    public Double getHeightTo() {
        return heightTo;
    }

    public void setHeightTo(Double heightTo) {
        this.heightTo = heightTo;
    }

    public Double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    public Set<OrganizerCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<OrganizerCategory> categories) {
        this.categories = categories;
    }

    public Set<Seller> getSeller() {
        return seller;
    }

    public void setSeller(Set<Seller> seller) {
        this.seller = seller;
    }

    public Set<Material> getMaterial() {
        return material;
    }

    public void setMaterial(Set<Material> material) {
        this.material = material;
    }
}
