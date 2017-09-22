package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Leverans;

/**
 * @author clalu4
 */
public interface LeveransRepository extends JpaRepository<Leverans, String> {

}