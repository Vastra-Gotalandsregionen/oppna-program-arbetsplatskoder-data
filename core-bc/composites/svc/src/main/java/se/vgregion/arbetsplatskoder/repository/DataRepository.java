package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.Data;

/**
 * @author Patrik Björk
 */
public interface DataRepository extends JpaRepository<Data, Integer> {
}
