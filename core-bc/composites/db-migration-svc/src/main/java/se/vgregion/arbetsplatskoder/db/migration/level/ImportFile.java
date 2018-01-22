package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.AbstractJob;
import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ImportFile {

    private final Map<String, Map<String, Set<Map<String, Object>>>> tree = newLevelsFromFile();

    private List<Map<String, Object>> items;

    private ImportFile() {
        super();
    }

    public static ImportFile getImportFile() {
        return new ImportFile();
    }

    public Map<String, Map<String, Set<Map<String, Object>>>> newLevelsFromFile() {
        try {
            return newLevelsFromFileImpl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Map<String, Map<String, Set<Map<String, Object>>>> newLevelsFromFileImpl() throws IOException {
        try (InputStream resource = AbstractLevels.class.getResourceAsStream("Concise_SumNiv_180115.csv")) {
            List<String> rows =
                    new BufferedReader(new InputStreamReader(resource,
                            StandardCharsets.ISO_8859_1)).lines().collect(Collectors.toList());
            final String[] columns = rows.get(0).split(Pattern.quote(";"));

            items = new ArrayList<>();

            Map<String, Map<String, Set<Map<String, Object>>>> levels = new HashMap() {
                @Override
                public Object get(Object key) {
                    if (!containsKey(key)) put(key, new HashMap() {
                        @Override
                        public Object get(Object key) {
                            if (!containsKey(key)) {
                                put(key, new HashSet<>());
                            }
                            return super.get(key);
                        }
                    });
                    return super.get(key);
                }
            };

            for (String row : rows.subList(1, rows.size())) {
                String[] values = row.split(Pattern.quote(";"));
                int i = 0;
                Map<String, Object> item = new HashMap<>();
                for (String column : columns) {
                    item.put(column, values[i++]);
                }
                items.add(item);
                levels.get(item.get("Summeringsnivå 1")).get(item.get("Summeringsnivå 2")).add(item);
            }
            return levels;
        }
    }

    public Map<String, Map<String, Set<Map<String, Object>>>> getTree() {
        return tree;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }


    public void findStuffAllreadyInTheDatabase() {
        ConnectionExt connection = AbstractJob.getMainConnectionExt();
        Prodn1Dao p1 = new Prodn1Dao(connection);
        Prodn2Dao p2 = new Prodn2Dao(connection);
        Prodn3Dao p3 = new Prodn3Dao(connection);

        int p1c = 0;
        int p2c = 0;
        int p3c = 0;

        Map<String, Map<String, Set<Map<String, Object>>>> roots = getTree();

        for (String rootName : roots.keySet()) {

            List<Map<String, Object>> p1inDb = p1.find(rootName);
            p1c += p1inDb.size();

            Map<String, Set<Map<String, Object>>> midLevel = roots.get(rootName);
            for (String midLevelName : midLevel.keySet()) {

                List<Map<String, Object>> p2inDb = p2.find(midLevelName);
                p2c += p2inDb.size();

                Set<Map<String, Object>> leafLevel = midLevel.get(midLevelName);

                TreeSet<String> leafLevelNames = new TreeSet<>();
                for (Map<String, Object> workPlaceCodeFileRow : leafLevel) {
                    leafLevelNames.add((String) workPlaceCodeFileRow.get("Summeringsnivå 3"));
                }
                for (String leafLevelName : leafLevelNames) {
                    List<Map<String, Object>> p3inDb = p3.find(leafLevelName);
                    p3c += p3inDb.size();
                }
            }
        }

        System.out.println("Antal dubletter i p1 " + p1c);
        System.out.println("Antal dubletter i p2 " + p2c);
        System.out.println("Antal dubletter i p3 " + p3c);
    }

  /*
    for (String rootName : roots.keySet()) {
      Map<String, Set<Map<String, Object>>> middLevel = roots.get(rootName);
      for (String middLevelName : middLevel.keySet()) {
        Set<Map<String, Object>> leafLevel = middLevel.get(middLevelName);
        for (Map<String, Object> workPlaceCodeFileRow : leafLevel) {

        }
      }
    }
   */

}
