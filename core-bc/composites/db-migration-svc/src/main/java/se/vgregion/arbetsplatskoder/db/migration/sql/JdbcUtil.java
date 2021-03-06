package se.vgregion.arbetsplatskoder.db.migration.sql;


import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Column;
import se.vgregion.arbetsplatskoder.db.migration.util.BeanMap;
import se.vgregion.arbetsplatskoder.db.migration.util.Zerial;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by clalu4 on 2014-08-09.
 */
public class JdbcUtil {

  public static Map<String, Object> keysToCamelCaseFormat(Map<String, Object> map) {
    Map<String, Object> result = new HashMap<>();
    for (String key : map.keySet())
      result.put(toCamelCase(key), map.get(key));
    return result;
  }

  public static List<Map<String, Object>> keysToCamelCaseFormatBatch(List<Map<String, Object>> maps) {
    List<Map<String, Object>> result = new ArrayList<>();
    for (Map<String, Object> item : maps)
      result.add(keysToCamelCaseFormat(item));
    return result;
  }

  public static List<Map<String, Object>> toMaps(ResultSet rs, int start, final int maxResultCount) {
    try {
      return toMapsImp(rs, start, maxResultCount);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static List<Map<String, Object>> toMapsImp(ResultSet rs, final int start, int maxResultCount) throws SQLException {
    List<Map<String, Object>> result = new ArrayList<>();
    ResultSetMetaData meta = rs.getMetaData();
    final int columnLimit = 1 + meta.getColumnCount();
    rs.absolute(start);
    while (rs.next()) {
      Map<String, Object> item = new HashMap<>();
      result.add(item);
      for (int i = 1; i < columnLimit; i++) {
        Object value = rs.getObject(i);
        if ((value instanceof Clob)) {
          value = Zerial.toText(((Clob) value));
        } else if (value instanceof Blob) {
          value = Zerial.toBytes((Blob) value);
        }
        if (value != null && (value instanceof String) && ((String) value).isEmpty())
          value = "";
        item.put(meta.getColumnName(i), value);
      }

      if (--maxResultCount <= 0) break;
    }
    rs.close();
    return result;
  }

  public static List<Map<String, Object>> toMaps(ResultSet rs) {
    try {
      return toMapsImp(rs);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<Map<String, Object>> toMapsImp(ResultSet rs) throws SQLException {
    List<Map<String, Object>> result = new ArrayList<>();
    ResultSetMetaData meta = rs.getMetaData();
    final int columnLimit = 1 + meta.getColumnCount();
    while (rs.next()) {
      Map<String, Object> item = new HashMap<>();
      result.add(item);
      for (int i = 1; i < columnLimit; i++) {
        Object value = rs.getObject(i);
        if (!(value instanceof Serializable) || value instanceof net.sourceforge.jtds.jdbc.ClobImpl) {
          if (value != null)
            System.out.println(value.getClass() + " is not zerializable");
        }
        if (value instanceof Clob) {
          value = Zerial.toText(((Clob) value).getAsciiStream());
        }
        item.put(meta.getColumnName(i), value);
      }
    }
    rs.close();
    return result;
  }

  public static List<Column> toColumnInfs(ResultSet resultSet) {

    try {
      List<Column> result = new ArrayList<>();
      ResultSetMetaData meta = resultSet.getMetaData();
      for (int i = 1, j = meta.getColumnCount() + 1; i < j; i++) {
        Column ci = new Column();
        result.add(ci);
        ci.setTableName(meta.getTableName(i));
        ci.setColumnName(meta.getColumnName(i));
        ci.setColumnClassName(meta.getColumnClassName(i));
        ci.setPrecision(meta.getPrecision(i));
        ci.setColumnDisplaySize(meta.getColumnDisplaySize(i));
        ci.setColumnType(meta.getColumnType(i));
        ci.setScale(meta.getScale(i));
        ci.setColumnLabel(meta.getColumnLabel(i));
        ci.setSchemaName(meta.getSchemaName(i));
        ci.setCatalogName(meta.getCatalogName(i));
        ci.setColumnTypeName(meta.getColumnTypeName(i));
        ci.setNullable(meta.isNullable(i) == ResultSetMetaData.columnNullable);
      }
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public static String toCamelCase(String s) {
    String[] parts = s.split(Pattern.quote("_"));
    String camelCaseString = "";
    for (String part : parts) {
      camelCaseString = camelCaseString + toProperCase(part.toLowerCase());
    }
    return camelCaseString.substring(0, 1).toLowerCase() +
        camelCaseString.substring(1);
  }

  public static <T> List<T> toBeans(ResultSet rs, Class<T> type) {
    try {
      List<T> result = new ArrayList<T>();
      List<Map<String, Object>> data = JdbcUtil.toMaps(rs);
      for (Map<String, Object> item : data) {
        T bean = type.newInstance();
        result.add(bean);
        BeanMap bm = new BeanMap(bean);
        for (String key : item.keySet()) {
          String beanKey = toCamelCase(key);
          if (bm.containsKey(beanKey))
            bm.put(beanKey, item.get(key));
        }
      }
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String toProperCase(String s) {
    return s.substring(0, 1).toUpperCase() +
        s.substring(1);
  }

}
