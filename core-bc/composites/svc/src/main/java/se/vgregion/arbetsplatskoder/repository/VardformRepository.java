package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Vardform;

/**
 * @author Patrik Björk
 */
public interface VardformRepository extends JpaRepository<Vardform, Integer> {

}
