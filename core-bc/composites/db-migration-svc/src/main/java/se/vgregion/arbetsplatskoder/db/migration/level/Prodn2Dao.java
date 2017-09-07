package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;

import java.util.HashMap;
import java.util.Map;

public class Prodn2Dao extends AbstractDao {

  /*public Prodn2Dao(ConnectionExt connection, String table) {
    super(connection, table);
  }*/

  public Prodn2Dao(ConnectionExt connection) {
    super(connection, "prodn2");
  }

  @Override
  Map<String, Object> create(Map<String, Object> parent, String newLevel) {
    Map<String, Object> item = new HashMap<>();
    item.put("id", sequence--);
    item.put("producentid", newLevel);
    item.put("avdelning", newLevel);
    item.put("kortnamn", newLevel);
    item.put("raderad", false);
    item.put("n1", parent.get("kortnamn"));
    //item.put("autoradering", withThatName);
    //item.put("riktvarde", withThatName);
    item.put("prodn1", parent.get("id"));
    connection.insert(table, item);
    cache.put(newLevel, item);
    insertCount++;
    return item;
  }

}
