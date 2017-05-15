package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;

import java.util.List;
import java.util.Set;

/**
 * @author Patrik Björk
 */
public interface Prodn3Repository extends JpaRepository<Prodn3, Integer> {

    Page<Prodn3> findAllByN2Equals(String n2, Pageable pageable);

    Page<Prodn3> findAllByN2In(List<String> n2s, Pageable pageable);

    Prodn3 findProdn3ByProducentidEquals(String producentid);

    Page<Prodn3> findAllByN2NotIn(Set<String> n2Producentids, Pageable pageable);

    Page<Prodn3> findAllByProducentidNotIn(Set<String> producentids, Pageable pageable);
}