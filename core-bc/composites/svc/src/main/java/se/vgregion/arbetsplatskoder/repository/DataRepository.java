package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import java.util.List;
import java.util.Set;

/**
 * @author Patrik Björk
 */
public interface DataRepository extends JpaRepository<Data, Integer> {

    @Query("select e from Data e where (lower(e.benamning) like :field1) or lower(e.arbetsplatskod) like :field1")
    Page<Data> advancedSearch(@Param("field1") String field1, Pageable page);

    @Query("select e from Data e where ((lower(e.benamning) like :field1) or lower(e.arbetsplatskod) like :field1) and e.prodn1 in :prodn1s")
    Page<Data> advancedSearchByProdn1In(@Param("field1") String field1, @Param("prodn1s") Set<Prodn1> prodn1s, Pageable page);

    Page<Data> findAllByProdn1In(Set<Prodn1> prodn1s, Pageable page);

    List<Data> findAllByProdn1IsNull();
}
