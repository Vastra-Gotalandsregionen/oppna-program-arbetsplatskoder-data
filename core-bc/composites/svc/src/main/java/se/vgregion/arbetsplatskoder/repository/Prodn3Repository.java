package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface Prodn3Repository extends JpaRepository<Prodn3, Integer> {

    List<Prodn3> findAllByN2Equals(String n2);

    Prodn3 findProdn3ByProducentidEquals(String producentid);
}