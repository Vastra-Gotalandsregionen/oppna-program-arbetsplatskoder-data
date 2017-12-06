package se.vgregion.arbetsplatskoder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.ArchivedData;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.DataExport;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkforsesamlmn;
import se.vgregion.arbetsplatskoder.export.repository.DataExportRepository;
import se.vgregion.arbetsplatskoder.export.repository.ViewApkForSesamLmnRepository;
import se.vgregion.arbetsplatskoder.repository.ArchivedDataRepository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.io.IOException;

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

    @Transactional(value = "exportTransactionManager")
    public void unexport(Integer withThatId) {
        dataExportRepository.delete(withThatId);
        viewApkForSesamLmnRepository.delete(withThatId.longValue());
    }

    @Transactional
    public Data saveAndArchive(Data that) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Data currentlyStoredData = dataRepository.findOne(that.getId());

            if (currentlyStoredData != null) {
                String json = mapper.writeValueAsString(currentlyStoredData);

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
            if (viewApkForSesamLmnRepository.exists(viewapkforsesamlmn.getId())) {
                viewApkForSesamLmnRepository.delete(viewapkforsesamlmn);
            }
        }
    }

}
