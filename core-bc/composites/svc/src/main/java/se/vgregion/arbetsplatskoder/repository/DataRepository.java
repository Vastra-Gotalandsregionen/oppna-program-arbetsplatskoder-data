package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.vgregion.arbetsplatskoder.domain.Data;

/**
 * @author Patrik Björk
 */
public interface DataRepository extends JpaRepository<Data, Integer> {

    @Query("select e from Data e where (lower(e.benamning) like :field1) or lower(e.arbetsplatskod) like :field1")
    Page<Data> advancedSearch(@Param("field1") String field1,
                               Pageable page);
}
