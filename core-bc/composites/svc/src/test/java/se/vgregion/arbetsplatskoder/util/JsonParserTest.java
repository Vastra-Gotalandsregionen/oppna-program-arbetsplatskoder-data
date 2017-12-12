package se.vgregion.arbetsplatskoder.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.arbetsplatskoder.domain.json.Unit;
import se.vgregion.arbetsplatskoder.domain.json.UnitsRoot;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Björk
 */
public class JsonParserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParserTest.class);

    @Test
    public void testParse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectReader objectReader = mapper.readerFor(UnitsRoot.class);

        UnitsRoot unitsRoot = objectReader.readValue(this.getClass().getClassLoader().getResourceAsStream("units-test.json"));

        LOGGER.info(unitsRoot.toString());

        Optional<Unit> found = unitsRoot.getUnits().stream()
                .filter(unit -> unit.getAttributes().getOu()[0].equals("efvd"))
                .findAny();

        assertTrue(found.isPresent());
    }
}
