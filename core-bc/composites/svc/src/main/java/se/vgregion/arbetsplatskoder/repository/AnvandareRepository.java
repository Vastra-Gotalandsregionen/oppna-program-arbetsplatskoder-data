package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Anvandare;

/**
 * @author Patrik Björk
 */
public interface AnvandareRepository extends JpaRepository<Anvandare, Integer> {

}