package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.Prodn2;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface Prodn2Repository extends JpaRepository<Prodn2, Integer> {

    List<Prodn2> findAllByN1Equals(String n1);

    Prodn2 findProdn2ByProducentidEquals(String producentId);
}