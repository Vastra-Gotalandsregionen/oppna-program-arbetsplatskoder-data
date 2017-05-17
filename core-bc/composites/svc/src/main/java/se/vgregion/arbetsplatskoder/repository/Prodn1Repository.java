package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface Prodn1Repository extends JpaRepository<Prodn1, Integer> {

    List<Prodn1> findAllByOrderByForetagsnamnAsc();

    @Query("select p from Prodn1 p where ((lower(p.foretagsnamn) like concat('%', :field1, '%')) or lower(p.id) like concat('%', :field1, '%')) order by p.foretagsnamn")
    List<Prodn1> search(@Param("field1") String field1);
}