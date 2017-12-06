package se.vgregion.arbetsplatskoder.export.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Ao3;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.DataExport;

/**
 * @author clalu4
 */
public interface Ao3ExportRepository extends JpaRepository<Ao3, Integer> {

    Ao3 findByAo3id(String ao3id);

}
