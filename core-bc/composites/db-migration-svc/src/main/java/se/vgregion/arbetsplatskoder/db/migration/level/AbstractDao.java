package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Table;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLevel {

  int sequence = -1;

  final Map<String, Map<String, Object>> cache = new HashMap<>();

  final ConnectionExt connection;

  final String table;

  int insertCount = 0;

  public AbstractLevel(ConnectionExt connection, String table) {
    this.connection = connection;
    this.table = table;
  }

  abstract Map<String, Object> create(Map<String, Object> parent, String newLevel);

}