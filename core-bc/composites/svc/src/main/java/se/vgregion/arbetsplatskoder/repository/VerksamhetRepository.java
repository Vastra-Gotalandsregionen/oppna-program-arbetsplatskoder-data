package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.Verksamhet;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface VerksamhetRepository extends JpaRepository<Verksamhet, Integer> {

    List<Verksamhet> findVerksamhetsByRaderadIsFalse();

}
