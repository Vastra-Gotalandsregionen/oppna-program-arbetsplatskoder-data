package se.vgregion.arbetsplatskoder.domain.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Erik Andersson
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Report {

    @JsonProperty("fileName")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
