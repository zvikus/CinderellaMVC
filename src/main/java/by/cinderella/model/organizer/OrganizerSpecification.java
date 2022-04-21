package by.cinderella.model.organizer;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Set;

public class OrganizerSpecification {

    public Specification<Organizer> nameLike(String name){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get(Organizer_.NAME), "%"+name+"%");
    }

    public static Specification<Organizer> priceBounds(Double from, Double to){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.between(root.get(Organizer_.PRICE), from, to);
    }
    public static Specification<Organizer> widthBounds(Double from, Double to){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.between(root.get(Organizer_.WIDTH), from, to);
    }
    public static Specification<Organizer> heightBounds(Double from, Double to){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.between(root.get(Organizer_.HEIGHT), from, to);
    }
    public static Specification<Organizer> lengthBounds(Double from, Double to){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.between(root.get(Organizer_.LENGTH), from, to);
    }

    public static Specification<Organizer> categoriesIn(Set<OrganizerCategory> categories) {


        return (root, query, criteriaBuilder)
                -> {
            query.distinct(true);
            /*query.distinct(true);
                Join<Organizer, OrganizerCategory> joinCategories = root.join("organizer_id");*/
                /*Join<Account, AccountRole> acctRolesJoin = joinUserAccount.join(Account_.accountRoles);
                Join<AccountRole, Role> rolesJoin = acctRolesJoin.join(AccountRole_.role);*/
            return root.join(Organizer_.CATEGORIES).in(categories);
        };

    }




}
