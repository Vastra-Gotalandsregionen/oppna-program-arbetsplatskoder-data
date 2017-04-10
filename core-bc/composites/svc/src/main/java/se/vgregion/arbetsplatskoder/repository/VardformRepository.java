package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.Ao3;
import se.vgregion.arbetsplatskoder.domain.Vardform;

/**
 * @author Patrik Björk
 */
public interface VardformRepository extends JpaRepository<Vardform, Integer> {

}
