package se.vgregion.arbetsplatskoder.domain.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Patrik Björk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit {

    @JsonProperty("dn")
    private String dn;

    @JsonProperty("attributes")
    private Attributes attributes;

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
