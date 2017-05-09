package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;

/**
 * @author Patrik Björk
 */
public interface Prodn3Repository extends JpaRepository<Prodn3, Integer> {

    Page<Prodn3> findAllByN2Equals(String n2, Pageable pageable);

    Prodn3 findProdn3ByProducentidEquals(String producentid);
}