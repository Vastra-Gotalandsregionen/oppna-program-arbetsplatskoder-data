package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.Agarform;

/**
 * @author Patrik Björk
 */
public interface AgarformRepository extends JpaRepository<Agarform, Integer> {

}