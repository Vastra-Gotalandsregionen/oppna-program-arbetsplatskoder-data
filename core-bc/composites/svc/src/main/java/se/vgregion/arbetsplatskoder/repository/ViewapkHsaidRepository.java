package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkwithao3Temp;

/**
 * @author Patrik Björk
 */
public interface ViewapkHsaidRepository extends JpaRepository<Viewapkwithao3Temp, Integer> {

}
