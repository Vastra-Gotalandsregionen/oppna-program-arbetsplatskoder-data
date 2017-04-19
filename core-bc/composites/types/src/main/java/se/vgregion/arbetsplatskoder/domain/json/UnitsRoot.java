package se.vgregion.arbetsplatskoder.domain.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Patrik Björk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitsRoot {

    @JsonProperty("units")
    private List<Unit> units;

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}
