package se.vgregion.arbetsplatskoder.db.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.db.migration.sql.Atom;
import se.vgregion.arbetsplatskoder.db.migration.sql.Junctor;
import se.vgregion.arbetsplatskoder.db.migration.sql.Match;
import se.vgregion.arbetsplatskoder.db.migration.util.BeanMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao object, general purpose for crud operations with jpa. So far only saving a new instance has been required.
 *
 * @author Claes Lundahl
 */
@SuppressWarnings("unchecked")
@Service
public class Crud {

    @PersistenceContext
    private EntityManager em;

    /**
     * Updates a entity in the db (jpa-persist operation).
     *
     * @param item to be persisted.
     * @param <T>  type of the param.
     * @return the instance just created.
     */
    @Transactional
    public <T> T update(T item) {
        item = em.merge(item);
        return item;
    }

    /**
     * Deletes a entity in the db (jpa-persist operation).
     *
     * @param item to be persisted.
     * @param <T>  type of the param.
     * @return the instance just created.
     */
    @Transactional
    public <T> T delete(T item) {
        em.remove(item);
        return item;
    }

    /**
     * Creates a new entity in the db (jpa-persist operation).
     *
     * @param item to be persisted.
     * @param <T>  type of the param.
     * @return the instance just created.
     */
    @Transactional
    public <T> T create(T item) {
        em.persist(item);
        return item;
    }

    @Transactional
    public <T> List<T> find(T byThatExample) {
        BeanMap bm = new BeanMap(byThatExample);
        StringBuilder sb = new StringBuilder();
        sb.append("select t from " + byThatExample.getClass().getSimpleName() + " t");

        StringBuilder condition = new StringBuilder();
        Junctor<Match> and = new Junctor<>(" and ");

        List values = new ArrayList();

        int i = 1;
        for (String s : bm.keySet()) {
            if (s.equalsIgnoreCase("class")) {
                continue;
            }
            Object value = bm.get(s);
            if (value != null) {
                if (value instanceof String) {
                    String asText = (String) value;
                    asText = asText.replace("*", "%");
                    value = asText;
                }
                Match match = null;

                if (value.toString().contains("%")) {
                    match = new Match(new Atom("lower(t." + s + ")"), " like ", new Atom("lower(?" + (i++) + ")"));
                } else {
                    match = new Match(new Atom("t." + s), value.toString().contains("%") ? " like " : " = ", new Atom("?" + (i++)));
                }

                and.add(match);
                values.add(value);
            }
        }

        if (!and.isEmpty()) {
            sb.append(" where ");
            and.toSql(sb, values);
        }

        Query q = em.createQuery(sb.toString());

        if (!values.isEmpty()) {
            i = 1;
            for (Object value : values) {
                q.setParameter(i++, value);
            }
        }

        return (List<T>) q.getResultList();
    }


}
