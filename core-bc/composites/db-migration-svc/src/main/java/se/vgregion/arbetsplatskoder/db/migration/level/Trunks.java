package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Table;

import java.util.HashMap;
import java.util.Map;

public class Trunk extends AbstractLevel {

  public Trunk(ConnectionExt connection, String table) {
    super(connection, table);
  }

  @Override
  Map<String, Object> create(Map<String, Object> parent, String newLevel) {
    Map<String, Object> item = new HashMap<>();
    item.put("id", sequence--);
    item.put("producentid", newLevel);
    item.put("foretagsnamn", newLevel);
    item.put("kortnamn", newLevel);
    item.put("raderad", false);
    connection.insert(table, item);
    this.cache.put(newLevel, item);
    return item;
  }

}
