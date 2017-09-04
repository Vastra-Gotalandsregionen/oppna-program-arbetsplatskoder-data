package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Levels {

  private final ConnectionExt connection;

  Trunk trunk; // = new Trunk(connection, "prodn1");
  Branch branch; // = new Branch(connection, "prodn2");
  Leaf leaf; // = new Leaf(connection, "prodn3");

  public Levels(ConnectionExt connection) {
    this.connection = connection;
    trunk = new Trunk(connection, "prodn1");
    branch = new Branch(connection, "prodn2");
    leaf = new Leaf(connection, "prodn3");
  }

  public void undoPreviousLevelCreation() {
    connection.execute("delete from prodn3 where id < 0");
    connection.execute("delete from prodn2 where id < 0");
    connection.execute("delete from prodn1 where id < 0");
    connection.commit();
  }

  public void copyLevelsFromFileIntoDatabase() {
    Map<String, Map<String, Set<String>>> fromFile = newLevelsFromFile();
    for (String rootName : fromFile.keySet()) {
      trunk.create(null, rootName);
      Map<String, Set<String>> middle = fromFile.get(rootName);
    }
  }

  private Map<String, Map<String, Set<String>>> newLevelsFromFile() {
    try {
      return newLevelsFromFileImpl();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static Map<String, Map<String, Set<String>>> newLevelsFromFileImpl() throws IOException {
    try (InputStream resource = LevelsOld.class.getResourceAsStream("14_SumnivåerArbetsplatser.csv")) {
      List<String> rows =
          new BufferedReader(new InputStreamReader(resource,
              StandardCharsets.ISO_8859_1)).lines().collect(Collectors.toList());
      final String[] columns = rows.get(0).split(Pattern.quote(";"));

      List<Map<String, String>> items = new ArrayList<>();

      Map<String, Map<String, Set<String>>> levels = new HashMap() {
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
        Map<String, String> item = new HashMap<>();
        for (String column : columns) {
          item.put(column, values[i++]);
        }
        items.add(item);
        levels.get(item.get("Summeringsnivå 1")).get(item.get("Summeringsnivå 2")).add(item.get("Summeringsnivå 3"));
      }
      return levels;
    }
  }

}
