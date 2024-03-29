package se.vgregion.arbetsplatskoder.repository.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.export.repository.DataExportRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by clalu4 on 2017-06-05.
 */
public class DataRepositoryImpl implements DataExtendedRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    @Autowired
    private DataExportRepository dataExportRepository;

/*
    @Query("select e from Data e where (lower(e.benamning) like :field1) or lower(e.arbetsplatskod) like :field1")
    Page<Data> advancedSearch(@Param("field1") String field1, Pageable page);
 */

    private <T> List<T> query(Class<T> clazz, String jpql, Pageable pageable, List words, Set<Prodn1> prodn1s) {
        TypedQuery<T> typedQuery = entityManager.createQuery(jpql, (Class) clazz);
        // System.out.println("Params are " + words);
        // System.out.println("Jpql are " + jpql);
        int i = 1;
        for (Object w : words) {
            typedQuery.setParameter(i++, w);
        }

        if (prodn1s != null && prodn1s.size() > 0) {
            typedQuery.setParameter(i++, prodn1s);
        }

        if (pageable != null) {
            typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        }
        List<T> results = typedQuery.getResultList();
        return results;
    }

    private List<String> toLikableWords(String ofThat) {
        if (ofThat == null || "".equals(ofThat)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        for (String s : ofThat.trim().split(Pattern.quote(" "))) {
            result.add((s.startsWith("%") ? "" : "%")
                    + s
                    + (s.endsWith("%") ? "" : "%"));
        }
        return result;
    }

    @Override
    public Page<Data> advancedSearch(String withTextFilter, Pageable pageable, Set<Prodn1> prodn1s, Date validToDate) {
        StringBuilder sb = new StringBuilder();
        List wordsToLookFor = toLikableWords(withTextFilter);
        List arguments = new ArrayList();

        sb.append(" where ");

        List<String> conditions = new ArrayList<>();
        conditions.add("1=1");

        int i = 1;
        List<String> allFields = Arrays.asList(
                "lower(d.arbetsplatskodlan)",
                "lower(d.benamning)",
                "lower(d.ansvar)",
                "function('to_char', d.tillDatum, 'YYYY-MM-DD')",
                "lower(d.prodn3.prodn2.kortnamn)",
                "lower(d.prodn1.kortnamn)"
        );
        for (Object s : wordsToLookFor) {
            List<String> allFieldsLikeCondtion = new ArrayList<>();
            for (String field : allFields) {
                allFieldsLikeCondtion.add(field + " like ?" + (i++));
                arguments.add(s);
            }
            conditions.add("(" + String.join(" or ", allFieldsLikeCondtion) + ")");
        }

        if (validToDate != null) {
            conditions.add("(d.tillDatum >= ?" + (i++) + "  or d.tillDatum is null)");
            arguments.add(validToDate);
        }

        if (prodn1s != null) {
            if (prodn1s.size() == 0) {
                // Should result in zero entities.
                conditions.add("1 = 2");
            } else {
                conditions.add("d.prodn1 in ?" + (i++));
                wordsToLookFor.add(prodn1s);
            }
        }

        sb.append(String.join(" and ", conditions));

        long count = query(
                Long.class,
                "select count(d.id) from " + Data.class.getSimpleName() + " d " + sb,
                null,
                arguments,
                prodn1s).get(0);

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

        List<Data> results = query(Data.class, jpql, pageable, arguments, prodn1s);

        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public Page<Data> advancedSearch(String withTextFilter, Pageable pageable) {
        return advancedSearch(withTextFilter, pageable, null, null);
    }

    private List<Map<String, Object>> query() {
        List<Map<String, Object>> result = new ArrayList<>();

        return result;
    }

    /*
    @Override
    public List<List<Object>> findStralforsTextualExportBatch() {
        List<List<Object>> rows = new ArrayList<>();
        String sql = "SELECT distinct arbetsplatskodlan, benamning, postadress, postnr, postort, \n" +
            " coalesce(leverans.leveranstext, 'Mölndal.') as leveranstext, \n" +
            " coalesce(fakturering.fakturering_kort_text, 'saknas') as faktureringstext \n" +
            "FROM data\n" +
            " left join fakturering on data.fakturering = fakturering.id                   \n" +
            " left join ao3 on data.ao3 = AO3.AO3id\n" +
            " left join leverans on data.leverans = leverans.id\n" +
            "WHERE \n" +
            " data.till_datum > current_timestamp and \n" +
            " data.apodos = false and  \n" +
            " arbetsplatskod <> '999999'";
        Query nq = entityManager.createNativeQuery(sql);
        List rl = nq.getResultList();
        for (Object os : rl) {
            Object[] row = (Object[]) os;
            rows.add(Arrays.asList(row));
            assert row.length == 7;
        }
        return rows;
    }
    */

    @Override
    public List<Data> findEhalsomyndighetensExportBatch() {
        String jpql = "select d\n" +
                " from Data d \n" +
                "where length(d.arbetsplatskod) < 12\n" +
                " and (d.tillDatum > :oneYearAgo or d.tillDatum is null)\n" +
                "order by d.arbetsplatskodlan";

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.YEAR, -1);
        Date oneYearAgo = cal.getTime();
        Query nq = entityManager.createQuery(jpql);
        nq.setParameter("oneYearAgo", oneYearAgo);
        List<Data> rows = nq.getResultList();
        return rows;
    }

    @Override
    public List<Data> findStralforsExportBatch() {
        String sql = "SELECT distinct d " +
                "FROM Data d\n" +
                "WHERE \n" +
                " (d.tillDatum > current_timestamp or d.tillDatum is null) and \n" +
                " (d.apodos = false or d.apodos is null) and  \n" +
                " arbetsplatskodlan != '14999999'" +
                " order by d.arbetsplatskodlan";
        Query nq = entityManager.createQuery(sql);
        List rl = nq.getResultList();
        return new ArrayList<>(rl);
    }

}
