package se.vgregion.arbetsplatskoder.domain.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Patrik Björk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes {

    @JsonProperty("hsaCountyCode")
    private String hsaCountyCode;

    @JsonProperty("hsaIdentity")
    private String[] hsaIdentity;

    @JsonProperty("vgrAo3kod")
    private String[] vgrAo3kod;

    @JsonProperty("ou")
    private String[] ou;

    @JsonProperty("vgrAnsvarsnummer")
    private String[] vgrAnsvarsnummer;

    @JsonProperty("hsaBusinessClassificationCode")
    private String[] hsaBusinessClassificationCode;

    @JsonProperty("hsaDestinationIndicator")
    private String[] hsaDestinationIndicator;

    @JsonProperty("hsaManagementCode")
    private String[] hsaManagementCode;

    @JsonProperty("hsaMunicipalityCode")
    private String[] hsaMunicipalityCode;

    @JsonProperty("hsaUnitPrescriptionCode")
    private String[] hsaUnitPrescriptionCode;

    @JsonProperty("vgrCareType")
    private String[] vgrCareType;

    @JsonProperty("vgrOrganizationalUnitNameShort")
    private String vgrOrganizationalUnitNameShort;

    @JsonProperty("hsaStreetAddress")
    private String[] hsaStreetAddress;

    public String getHsaCountyCode() {
        return hsaCountyCode;
    }

    public void setHsaCountyCode(String hsaCountyCode) {
        this.hsaCountyCode = hsaCountyCode;
    }

    public String[] getHsaIdentity() {
        return hsaIdentity;
    }

    public void setHsaIdentity(String[] hsaIdentity) {
        this.hsaIdentity = hsaIdentity;
    }

    public String[] getVgrAo3kod() {
        return vgrAo3kod;
    }

    public void setVgrAo3kod(String[] vgrAo3kod) {
        this.vgrAo3kod = vgrAo3kod;
    }

    public String[] getVgrAnsvarsnummer() {
        return vgrAnsvarsnummer;
    }

    public void setVgrAnsvarsnummer(String[] vgrAnsvarsnummer) {
        this.vgrAnsvarsnummer = vgrAnsvarsnummer;
    }

    public String[] getOu() {
        return ou;
    }

    public void setOu(String[] ou) {
        this.ou = ou;
    }

    public String[] getHsaBusinessClassificationCode() {
        return hsaBusinessClassificationCode;
    }

    public void setHsaBusinessClassificationCode(String[] hsaBusinessClassificationCode) {
        this.hsaBusinessClassificationCode = hsaBusinessClassificationCode;
    }

    public String[] getHsaDestinationIndicator() {
        return hsaDestinationIndicator;
    }

    public void setHsaDestinationIndicator(String[] hsaDestinationIndicator) {
        this.hsaDestinationIndicator = hsaDestinationIndicator;
    }

    public String[] getHsaManagementCode() {
        return hsaManagementCode;
    }

    public void setHsaManagementCode(String[] hsaManagementCode) {
        this.hsaManagementCode = hsaManagementCode;
    }

    public String[] getHsaMunicipalityCode() {
        return hsaMunicipalityCode;
    }

    public void setHsaMunicipalityCode(String[] hsaMunicipalityCode) {
        this.hsaMunicipalityCode = hsaMunicipalityCode;
    }

    public String[] getHsaUnitPrescriptionCode() {
        return hsaUnitPrescriptionCode;
    }

    public void setHsaUnitPrescriptionCode(String[] hsaUnitPrescriptionCode) {
        this.hsaUnitPrescriptionCode = hsaUnitPrescriptionCode;
    }

    public String[] getVgrCareType() {
        return vgrCareType;
    }

    public void setVgrCareType(String[] vgrCareType) {
        this.vgrCareType = vgrCareType;
    }

    public String getVgrOrganizationalUnitNameShort() {
        return vgrOrganizationalUnitNameShort;
    }

    public void setVgrOrganizationalUnitNameShort(String vgrOrganizationalUnitNameShort) {
        this.vgrOrganizationalUnitNameShort = vgrOrganizationalUnitNameShort;
    }

    public String[] getHsaStreetAddress() {
        return hsaStreetAddress;
    }

    public void setHsaStreetAddress(String[] hsaStreetAddress) {
        this.hsaStreetAddress = hsaStreetAddress;
    }
}
