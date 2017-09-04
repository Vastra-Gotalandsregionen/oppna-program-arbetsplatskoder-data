package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Table;

import java.util.HashMap;
import java.util.Map;

public class Level {
  int sequence = -1;
  final Map<String, Map<String, Object>> cache = new HashMap<>();
  final Table table;
  public Level(Table table) {
    this.table = table;
  }
}