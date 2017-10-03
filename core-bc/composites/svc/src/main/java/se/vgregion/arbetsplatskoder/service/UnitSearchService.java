package se.vgregion.arbetsplatskoder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import se.vgregion.arbetsplatskoder.domain.json.Unit;
import se.vgregion.arbetsplatskoder.domain.json.UnitsRoot;
import se.vgregion.arbetsplatskoder.util.ConvenientSslContextFactory;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrik Björk
 */
@Service
public class UnitSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitSearchService.class);

    private UnitsRoot unitsRoot;

    @Value("${kiv.units.url}")
    private String kivUnitsUrl;

    @Value("${trustStore}")
    private String trustStore;

    @Value("${trustStorePassword}")
    private String trustStorePassword;

    @Value("${keyStore}")
    private String keyStore;

    @Value("${keyStorePassword}")
    private String keyStorePassword;

    @Value("${trustStoreType}")
    private String trustStoreType;

    @Value("${keyStoreType}")
    private String keyStoreType;

    @PostConstruct
    public void init() throws IOException {
        try {
            updateUnits();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 25 6 * * *")
    public void update() {
        try {
            updateUnits();
            LOGGER.info("Updated units.");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    void updateUnits() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectReader objectReader = mapper.readerFor(UnitsRoot.class);

        if (!StringUtils.isEmpty(kivUnitsUrl)) {
            URL url = new URL(kivUnitsUrl);
            URLConnection urlConnection = url.openConnection();

            if (urlConnection instanceof HttpsURLConnection) {
                HttpsURLConnection connection = (HttpsURLConnection) urlConnection;

                connection.setSSLSocketFactory(new ConvenientSslContextFactory(trustStore, trustStorePassword,
                        trustStoreType,
                        keyStore,
                        keyStorePassword,
                        keyStoreType).createSslContext().getSocketFactory());
            }

            try (InputStream inputStream = urlConnection.getInputStream()) {
                this.unitsRoot = objectReader.readValue(inputStream);
            }
        } else {
            LOGGER.info("No kivUnitsUrl set. Skipping units update.");
        }
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
