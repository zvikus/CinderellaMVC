package by.cinderella.model.organizer;


import javax.annotation.Generated;
import javax.persistence.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Organizer_.class)
public abstract class Organizer_ {
    public static volatile SingularAttribute<Organizer, String> id;
    public static volatile SingularAttribute<Organizer, String> name;

    public static volatile SingularAttribute<Organizer, String> length;
    public static volatile SingularAttribute<Organizer, String> width;
    public static volatile SingularAttribute<Organizer, String> height;

    public static volatile SingularAttribute<Organizer, String> price;

    public static volatile SingularAttribute<Organizer, Material> material;
    public static volatile SingularAttribute<Organizer, Set<OrganizerCategory>> categories;
    public static volatile SingularAttribute<Organizer, Seller> seller;


    public static final String ID = "id";
    public static final String NAME = "name";

    public static final String LENGTH = "length";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";

    public static final String PRICE = "price";

    public static final String MATERIAL = "material";
    public static final String CATEGORIES = "categories";
    public static final String SELLER = "seller";

}
