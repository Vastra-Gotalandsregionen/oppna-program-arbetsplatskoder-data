package se.vgregion.arbetsplatskoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Ao3;
import se.vgregion.arbetsplatskoder.export.repository.Ao3ExportRepository;
import se.vgregion.arbetsplatskoder.repository.Ao3Repository;

@Component
public class Ao3Operations {

    @Autowired
    Ao3ExportRepository ao3ExportRepository;

    @Autowired
    Ao3Repository ao3Repository;

    @Transactional(value = "exportTransactionManager")
    public void export(Ao3 that) {
        ao3ExportRepository.save(that);
    }

    @Transactional
    public Ao3 save(Ao3 that) {
        return ao3Repository.save(that);
    }

}
