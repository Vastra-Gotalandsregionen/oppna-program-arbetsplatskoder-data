package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.Prodn1;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface Prodn1Repository extends JpaRepository<Prodn1, Integer> {

    List<Prodn1> findAllByOrderByForetagsnamn();

    Prodn1 findProdn1ByProducentidEquals(String producentId);
}