package se.vgregion.arbetsplatskoder.repository.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.ArchivedData;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by clalu4 on 2017-06-05.
 */
public class DataRepositoryImpl implements DataExtendedRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

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

        if (prodn1s != null) {
            typedQuery.setParameter(i++, prodn1s);
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
        List arguments = new ArrayList();

        if (wordsToLookFor.size() > 0) {
            sb.append(" where ");
            List<String> conditions = new ArrayList<>();
            int i = 1;
            List<String> allFields = Arrays.asList(
                "lower(d.arbetsplatskodlan)",
                "lower(d.benamning)",
                "function('to_char', d.regDatum, 'YYYY-MM-DD')",
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
            if (prodn1s != null) {
                conditions.add("d.prodn1 in ?" + (i++));
                wordsToLookFor.add(prodn1s);
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

        throw new RuntimeException();
    }

    @Override
    public Page<Data> advancedSearch(String withTextFilter, Pageable pageable) {
        return advancedSearch(withTextFilter, pageable, null);
    }

    @Override
    @Transactional
    public Data saveAndArchive(Data data) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            Data currentlyStoredData = entityManager.find(Data.class, data.getId());

            if (currentlyStoredData != null) {
                String json = mapper.writeValueAsString(currentlyStoredData);

                ArchivedData archivedData = mapper.readValue(json, ArchivedData.class);

                archivedData.setId(null);

                archivedData.setReplacer(data);

                entityManager.merge(archivedData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return entityManager.merge(data);
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
            " and d.tillDatum > :oneYearAgo\n" +
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
            " d.tillDatum > current_timestamp and \n" +
            " d.apodos = false and  \n" +
            " arbetsplatskod <> '999999'";
        Query nq = entityManager.createQuery(sql);
        List rl = nq.getResultList();
        return new ArrayList<>(rl);
    }

}
