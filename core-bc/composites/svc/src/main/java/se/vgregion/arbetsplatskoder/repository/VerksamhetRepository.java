package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Verksamhet;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface VerksamhetRepository extends JpaRepository<Verksamhet, Integer> {

    List<Verksamhet> findVerksamhetsByRaderadIsFalse();

}
