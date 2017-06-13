package se.vgregion.arbetsplatskoder.repository.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by clalu4 on 2017-06-05.
 */
public class DataRepositoryImpl implements DataExtendedRepository {

  @Autowired
  private EntityManager entityManager;

/*
    @Query("select e from Data e where (lower(e.benamning) like :field1) or lower(e.arbetsplatskod) like :field1")
    Page<Data> advancedSearch(@Param("field1") String field1, Pageable page);
 */

  private <T> List<T> query(Class<T> clazz, String jpql, Pageable pageable, List words) {
    TypedQuery<T> typedQuery = entityManager.createQuery(jpql, (Class) clazz);
    int i = 1;
    for (Object w : words) {
      typedQuery.setParameter(i++, w);
    }
    if (pageable != null) {
      typedQuery.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());
    }
    List<T> results = typedQuery.getResultList();
    return results;
  }

  private List<String> toLikableWords(String ofThat) {
    List<String> result = new ArrayList<>();
    for (String s : ofThat.trim().split(Pattern.quote(" "))) {
      result.add((s.startsWith("%") ? "" : "%")
          + s
          + (s.endsWith("%") ? "" : "%"));
    }
    return result;
  }

  @Override
  public Page<Data> advancedSearch(String withTextFilter, Pageable pageable, Set<Prodn1> prodn1s) {
    StringBuilder sb = new StringBuilder();
    List wordsToLookFor = toLikableWords(withTextFilter);

    if (wordsToLookFor.size() > 0) {
      sb.append(" where ");
      List<String> conditions = new ArrayList<>();
      int i = 1;
      for (Object s : wordsToLookFor) {
        conditions.add("(concat(' ', lower(d.benamning), ' ', d.arbetsplatskod, ' ', " +
            "function('to_char', d.tillDatum, 'YYYY-MM-DD'), ' ', d.prodn3.prodn2.kortnamn, ' ', d.prodn3.prodn2.prodn1.kortnamn, ' ') like ?" + (i++) + ")");
      }
      if (prodn1s != null) {
        conditions.add("d.prodn3.prodn2.prodn1 in ?" + (i++));
        wordsToLookFor.add(prodn1s);
      }
      sb.append(String.join(" and ", conditions));

      long count = query(
          Long.class,
          "select count(d.id) from " + Data.class.getSimpleName() + " d " + sb,
          null,
          wordsToLookFor).get(0);

      if (pageable.getSort() != null) {
        Sort sort = pageable.getSort();
        List<String> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
          orders.add("d." + order.getProperty() + " " + order.getDirection().name());
        }
        if (!orders.isEmpty()) {
          sb.append(" order by ");
          sb.append(String.join(", ", orders));
        }
      }

      String jpql = "select d from "
          + Data.class.getSimpleName()
          + " d "
          + sb.toString();

      List<Data> results = query(Data.class, jpql, pageable, wordsToLookFor);

      return new PageImpl<>(results, pageable, count);
    }

    throw new RuntimeException();
  }

  @Override
  public Page<Data> advancedSearch(String withTextFilter, Pageable pageable) {
    return advancedSearch(withTextFilter, pageable, null);
  }


}
