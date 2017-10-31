package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;

import java.util.HashMap;
import java.util.Map;

public class Prodn3Dao extends AbstractDao {


  public Prodn3Dao(ConnectionExt connection) {
    super(connection, "prodn3");
  }

  @Override
  Map<String, Object> create(Map<String, Object> parent, String newLevel) {
    Map<String, Object> item = new HashMap<>();
    item.put("id", sequence--);
    item.put("producentid", newLevel);
    item.put("kortnamn", newLevel);
    item.put("raderad", false);
    item.put("n2", parent.get("kortnamn"));
    item.put("autoradering", false);
    item.put("prodn2", parent.get("id"));
    connection.insert(table, item);
    cache.put(newLevel, item);
    insertCount++;
    return item;
  }

}
