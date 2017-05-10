package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface Prodn2Repository extends JpaRepository<Prodn2, Integer> {

    Page<Prodn2> findAllByN1Equals(String n1, Pageable pageable);

    Prodn2 findProdn2ByProducentidEquals(String producentId);

    @Query("select p from Prodn2 p where ((lower(p.avdelning) like concat('%', :field1, '%')) or lower(p.producentid) like concat('%', :field1, '%'))")
    List<Prodn2> search(@Param("field1") String field1);

}