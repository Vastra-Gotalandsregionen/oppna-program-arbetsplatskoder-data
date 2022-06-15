package se.vgregion.arbetsplatskoder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.ArchivedData;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.*;
import se.vgregion.arbetsplatskoder.export.repository.DataExportRepository;
import se.vgregion.arbetsplatskoder.export.repository.ViewApkForSesamLmnRepository;
import se.vgregion.arbetsplatskoder.export.repository.ViewapkHsaidRepository;
import se.vgregion.arbetsplatskoder.export.repository.Viewapkwithao3Repository;
import se.vgregion.arbetsplatskoder.repository.Ao3Repository;
import se.vgregion.arbetsplatskoder.repository.ArchivedDataRepository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.io.IOException;
import java.util.Optional;

@Component
public class DataOperations {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataExportRepository dataExportRepository;

    @Autowired
    private ArchivedDataRepository archivedDataRepository;

    @Autowired
    private ViewApkForSesamLmnRepository viewApkForSesamLmnRepository;

    @Autowired
    private ViewapkHsaidRepository viewapkHsaidRepository;

    @Autowired
    private Viewapkwithao3Repository viewapkwithao3Repository;

    @Autowired
    private Ao3Repository ao3Repository;

    @Transactional(value = "exportTransactionManager")
    public void unexport(Integer withThatId) {
        dataExportRepository.deleteById(withThatId);
        viewApkForSesamLmnRepository.deleteById(withThatId.longValue());
        viewapkHsaidRepository.deleteById(withThatId);
        viewapkwithao3Repository.deleteById(withThatId);
    }

    @Transactional
    public Data saveAndArchive(Data that) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Optional<Data> currentlyStoredData = dataRepository.findById(that.getId());

            if (currentlyStoredData.isPresent()) {
                String json = mapper.writeValueAsString(currentlyStoredData.get());

                ArchivedData archivedData = mapper.readValue(json, ArchivedData.class);

                archivedData.setId(null);

                archivedData.setReplacer(that);

                archivedDataRepository.save(archivedData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dataRepository.save(that);
    }

    @Transactional(value = "exportTransactionManager")
    public void export(Data that) {
        DataExport export = new DataExport(that);
        dataExportRepository.save(export);
        Viewapkforsesamlmn viewapkforsesamlmn = new Viewapkforsesamlmn(that);
        if (viewapkforsesamlmn.isOkToAppearInExportView()) {
            viewApkForSesamLmnRepository.save(viewapkforsesamlmn);
        } else {
            if (viewApkForSesamLmnRepository.existsById(viewapkforsesamlmn.getId())) {
                viewApkForSesamLmnRepository.delete(viewapkforsesamlmn);
            }
        }

        ViewapkHsaid viewapkHsaid = new ViewapkHsaid(that);
        if (viewapkHsaid.isOkToAppearInExportView()) {
            viewapkHsaidRepository.save(viewapkHsaid);
        } else {
            viewapkHsaidRepository.delete(viewapkHsaid);
        }

        viewapkwithao3Repository.save(
                new Viewapkwithao3().loadData(that).loadData(ao3Repository.findByAo3id(that.getAo3()))
        );
    }

}
