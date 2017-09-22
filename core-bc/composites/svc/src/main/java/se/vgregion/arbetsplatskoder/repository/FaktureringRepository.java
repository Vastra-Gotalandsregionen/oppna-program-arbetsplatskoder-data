package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Fakturering;

/**
 * @author clalu4
 */
public interface FaktureringRepository extends JpaRepository<Fakturering, String> {

}