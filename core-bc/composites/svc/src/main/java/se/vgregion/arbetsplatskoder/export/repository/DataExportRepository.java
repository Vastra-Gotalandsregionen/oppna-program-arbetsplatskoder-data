package se.vgregion.arbetsplatskoder.export.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.DataExport;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkwithao3;
import se.vgregion.arbetsplatskoder.export.repository.extension.Viewapkwithao3ExtendedRepository;

/**
 * @author clalu4
 */
public interface DataExportRepository extends JpaRepository<DataExport, Integer> {

}
