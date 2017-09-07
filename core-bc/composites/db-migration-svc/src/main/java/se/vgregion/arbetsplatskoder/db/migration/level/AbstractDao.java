package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao {

  int sequence = -1;

  final Map<String, Map<String, Object>> cache = new HashMap<>();

  final ConnectionExt connection;

  final String table;

  int insertCount = 0;

  public AbstractDao(ConnectionExt connection, String table) {
    this.connection = connection;
    this.table = table;
  }

  abstract Map<String, Object> create(Map<String, Object> parent, String newLevel);

  public List<Map<String, Object>> find(String thatShortName) {
    return connection.query("select * from " + table + " where kortnamn = ? and raderad = false", 0, 100, thatShortName);
  }

}