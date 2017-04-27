package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Agarform;

/**
 * @author Patrik Björk
 */
public interface AgarformRepository extends JpaRepository<Agarform, Integer> {

}