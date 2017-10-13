package se.vgregion.arbetsplatskoder.export.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkforsesamlmn;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkwithao3;
import se.vgregion.arbetsplatskoder.export.repository.extension.Viewapkwithao3ExtendedRepository;

/**
 * @author clalu4
 */
public interface ViewApkForSesamLmnRepository extends JpaRepository<Viewapkforsesamlmn, Integer> {

}
