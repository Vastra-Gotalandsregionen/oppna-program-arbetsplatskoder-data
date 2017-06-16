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
    private String hmac;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getHmac() {
        return hmac;
    }
}
