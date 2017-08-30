package se.vgregion.arbetsplatskoder.repository.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Patrik Björk
 */

public class Prodn3RepositoryImpl implements Prodn3ExtendedRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Prodn3> findProdn3s(Set<Integer> notInProdn3Ids, Collection<Prodn2> inProdn2s, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Prodn3> criteriaQuery = cb.createQuery(Prodn3.class);

        Root<Prodn3> root = criteriaQuery.from(Prodn3.class);

        Path<Object> id = root.get("id");
        Path<Object> prodn2 = root.get("prodn2");

        Predicate whereContent = cb.equal(cb.literal(1), 1);
        criteriaQuery.where(whereContent);

        if (notInProdn3Ids != null && notInProdn3Ids.size() > 0) {
            Predicate inClausePredicate = id.in(notInProdn3Ids).not();
            whereContent = cb.and(whereContent, inClausePredicate);
        }

        if (inProdn2s != null) {
            Predicate prodn2InPredicate = prodn2.in(inProdn2s);
            whereContent = cb.and(whereContent, prodn2InPredicate);
        }

        criteriaQuery.select(root);
        criteriaQuery.where(whereContent);
        Order order1 = cb.asc(cb.lower(root.get("prodn2").get("prodn1").get("kortnamn")));
        Order order2 = cb.asc(cb.lower(root.get("prodn2").get("kortnamn")));
        Order order3 = cb.asc(cb.lower(root.get("kortnamn")));
        criteriaQuery.orderBy(order1, order2, order3);

        TypedQuery<Prodn3> typedQuery = entityManager.createQuery(criteriaQuery);

        if (pageable != null) {
            typedQuery.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());
        }

        List<Prodn3> resultList = typedQuery.getResultList();

                // Do the same but for count
        CriteriaQuery<Long> countCriteriaQuery = cb.createQuery(Long.class)
                .select(cb.count(root))
                .where(whereContent);

        countCriteriaQuery.from(Prodn3.class);

        Long count = entityManager.createQuery(countCriteriaQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, count);
    }
}
