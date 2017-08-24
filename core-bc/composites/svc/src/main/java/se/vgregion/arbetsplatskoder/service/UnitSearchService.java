package se.vgregion.arbetsplatskoder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.stereotype.Service;
import se.vgregion.arbetsplatskoder.domain.json.Unit;
import se.vgregion.arbetsplatskoder.domain.json.UnitsRoot;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrik Björk
 */
@Service
public class UnitSearchService {

    private UnitsRoot unitsRoot;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectReader objectReader = mapper.readerFor(UnitsRoot.class);

        this.unitsRoot = objectReader.readValue(this.getClass().getClassLoader().getResourceAsStream("units.json"));
    }

    public List<Unit> searchUnits(String query) {
        if (query == null) {
            throw new IllegalArgumentException("searchTerm must not be null");
        }

        if (this.unitsRoot == null) {
            throw new RuntimeException("UnitsRoot is not intialized.");
        }

        List<Unit> results = new ArrayList<>();

        for (Unit unit : this.unitsRoot.getUnits()) {
            String[] ou = unit.getAttributes().getOu();
            String[] hsaIdentity = unit.getAttributes().getHsaIdentity();
            if (ou != null && ou.length > 0 && matches(ou[0], query.toLowerCase())) {
                results.add(unit);
            } else if (hsaIdentity != null && hsaIdentity.length > 0 && hsaIdentity[0].toLowerCase().contains(query.toLowerCase())) {
                results.add(unit);
            }
        }

        return results;
    }

    private boolean matches(String text, String query) {
        String[] split = query.split(" ");

        for (String word : split) {
            if (!text.toLowerCase().contains(word)) {
                return false;
            }
        }

        return true;
    }
}
