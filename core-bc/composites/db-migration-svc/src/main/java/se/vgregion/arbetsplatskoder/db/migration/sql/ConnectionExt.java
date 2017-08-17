package se.vgregion.arbetsplatskoder.db.migration.sql;

import se.vgregion.arbetsplatskoder.db.migration.sql.meta.*;
import se.vgregion.arbetsplatskoder.db.migration.util.BeanMap;
import se.vgregion.arbetsplatskoder.db.migration.util.ObjectUtil;

import java.sql.*;
import java.util.*;

public class ConnectionExt {

  private String user, password, url, driver;

  private Connection connection = null;
  private PreparedStatement currentRun;
  private String databaseProductName;
  private List<Type> types;

  public ConnectionExt(String url, String user, String password, String driver) {
    super();
    this.user = user;
    this.password = password;
    this.url = url;
    this.driver = driver;
    connection = connect();
  }

  public ConnectionExt(Connection connection) {
    super();
    this.connection = connection;
  }

  private Connection connect() {
    try {
      return connectImp();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Connection connectImp() throws ClassNotFoundException, SQLException {
    Class.forName(driver);
    Connection connection = DriverManager.getConnection(url, user, password);
    connection.setAutoCommit(false);
    return connection;
  }

  public List<Map<String, Object>> query(String question, final int start, final int maxResultCount, Object... withParameters) {
    try {
      return JdbcUtil.toMaps(getResultSetByQuery(question, withParameters), start, maxResultCount);
    } finally {
      if (currentRun != null) {
        try {
          currentRun.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        } finally {
          currentRun = null;
        }
      }
    }
  }

  private ResultSet getResultSetByQuery(String sql, Object... values) {
    try {
      return getResultSetByQueryImp(sql, values);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private ResultSet getResultSetByQueryImp(String sql, Object... values) throws SQLException {
    PreparedStatement ps = null;
    try {
      ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);
      int i = 1;
      for (Object value : values) {
        ps.setObject(i++, value);
      }
      return ps.executeQuery();
    } finally {
      //if (ps != null) ps.close();
      currentRun = ps;
    }
  }

  public boolean execute(String script) {
    PreparedStatement ps = null;
    try {
      ps = connection.prepareStatement(script);
      boolean r = ps.execute();
      ps.close();
      return r;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean execute(String script, Object... values) {
    PreparedStatement ps = null;
    try {
      ps = connection.prepareStatement(script);
      int index = 1;
      for (Object value : values) {
        ps.setObject(index++, value);
      }
      boolean r = ps.execute();
      ps.close();
      return r;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int insert(String intoTable, Map<String, Object> tupel) {
    // INSERT INTO products (name, price, product_no) VALUES ('Cheese', 9.99, 1);
    try {
      String columns = tupel.keySet().toString();
      columns = columns.substring(1, columns.length() - 1);
      StringBuilder args = new StringBuilder("?");
      for (int i = 1, j = tupel.keySet().size(); i < j; i++)
        args.append(", ?");

      String sql = "insert into "
          + intoTable
          + " (" + columns + ") values ("
          + args + ")";

      PreparedStatement ps = connection.prepareCall(
          sql);

      int c = 1;
      for (String key : tupel.keySet()) {
        ps.setObject(c++, tupel.get(key));
      }

      return ps.executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void commit() {
    try {
      connection.commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void rollback() {
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public String getDriver() {
    return driver;
  }

  public String getPassword() {
    return password;
  }

  public String getUrl() {
    return url;
  }

  public String getUser() {
    return user;
  }

  public List<Schema> getSchemas() {
    try {
      return Schema.toSchemas(getAllTables());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<String> getSchemaNames() {
    List<String> result = new ArrayList<>();

    return result;
  }

  public List<Schema> getSchemas(String... withSchemaPattern) {
    try {
      List<Schema> result = new ArrayList<>();
      for (String s : withSchemaPattern) result.addAll(Schema.toSchemas(getAllTablesImpl(s)));
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<Table> getAllTables() {
    try {
      return getAllTablesImpl();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Table> getAllTablesImpl() throws SQLException {
    return getAllTablesImpl(null);
  }

  private List<Table> getAllTablesImpl(String withSchemaName) throws SQLException {
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet rs = meta.getTables(
        null,
        withSchemaName,
        null,
        null
    );
    List<Table> result = JdbcUtil.toBeans(rs, Table.class);
    List<Table> justTablesAndViews = new ArrayList<>();
    for (Table ti : result) {
      String type = ObjectUtil.def(ti.getTableType(), "").toLowerCase();
      if (type.contains("table") || type.contains("view")) {
        List<Privilege> privs = JdbcUtil.toBeans(
            meta.getTablePrivileges(ti.getTableCatalog(), ti.getTableSchema(), ti.getTableName()),
            Privilege.class
        );
        List<String> privNames = new ArrayList<>();
        for (Privilege priv : privs) privNames.add(priv.getPrivilegeType());
        ti.setPrivileges(privs);

        if (privNames.isEmpty() || privNames.contains("select") || privNames.contains("SELECT"))
          justTablesAndViews.add(ti);
      }
    }
    result = justTablesAndViews;

    for (Table t1 : new ArrayList<>(result)) {
      if (!ObjectUtil.isEmptyOrNull(withSchemaName)) {
        if (withSchemaName.equalsIgnoreCase(t1.getTableSchema()) || t1.getTableSchema().matches(withSchemaName)) {
          // Do nothing.
        } else {
          // Skip this.
          result.remove(t1);
          continue;
        }
      }

      rs = meta.getPrimaryKeys(t1.getTableCatalog(), t1.getTableSchema(), t1.getTableName());
      t1.getPrimaries().addAll(JdbcUtil.toBeans(rs, Primary.class));
      Set<String> prims = new HashSet<>();
      for (Primary prim : t1.getPrimaries()) {
        prims.add(prim.getColumnName());
      }

      try {
        PreparedStatement ps = connection.prepareStatement(
            "select * from " + t1.getTableSchema() + "." + t1.getTableName() + " where 'foo' = 'baa'");

        rs = ps.executeQuery();
        t1.getColumns().addAll(JdbcUtil.toColumnInfs(rs));
        rs.close();
        ps.close();
      } catch (SQLException sqle) {

      }

      for (Column column : t1.getColumns()) {
        column.setPrimary(prims.contains(column.getColumnName()));
      }

      List<Reference.Mapping> eks = JdbcUtil.toBeans(
          meta.getExportedKeys(t1.getTableCatalog(), t1.getTableSchema(), t1.getTableName()), Reference.Mapping.class
      );
      t1.getExportedKeys().addAll(toReferenceInfs(eks));
      for (Reference reference : t1.getExportedKeys()) {
        reference.setPrimaryTable(t1);
        reference.setForeignTable(findTable(result, reference.getFktableSchem(), reference.getFktableName()));
      }

      List<Reference.Mapping> iks = JdbcUtil.toBeans(
          meta.getImportedKeys(t1.getTableCatalog(), t1.getTableSchema(), t1.getTableName()), Reference.Mapping.class
      );
      t1.getImportedKeys().addAll(toReferenceInfs(iks));
      for (Reference reference : t1.getImportedKeys()) {
        reference.setForeignTable(t1);
        reference.setPrimaryTable(findTable(result, reference.getPktableSchem(), reference.getPktableName()));
      }
    }

    return result;
  }

  private Table findTable(List<Table> inHere, String bySchemaName, String andTableName) {
    for (Table table : inHere) {
      if (ObjectUtil.equals(table.getTableSchema(), bySchemaName) &&
          ObjectUtil.equals(table.getTableName(), andTableName)) {
        return table;
      }
    }
    return null;
  }

  private List<Reference> toReferenceInfs(List<Reference.Mapping> eks) {
    List<Reference> result = new ArrayList<>();

    if (!eks.isEmpty()) {
      Reference ref = null;
      for (Reference.Mapping ek : eks) {
        if (ek.getKeySeq() == 1) {
          ref = new Reference();
          new BeanMap(ref).putAllApplicable(new BeanMap(ek));
          result.add(ref);
        }
        ref.getMappings().add(ek);
      }
    }

    return result;
  }

  public List<Type> getTypes() {
    try {
      if (types == null) {
        types = JdbcUtil.toBeans(connection.getMetaData().getTypeInfo(), Type.class);
      }
      return types;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Type getType(String byItsName) {
    for (Type type : getTypes()) {
      if (byItsName.equals(type.getTypeName()))
        return type;
    }
    return null;
  }

  public String getDatabaseProductName() {
    try {
      if (databaseProductName == null)
        return databaseProductName = connection.getMetaData().getDatabaseProductName();
      return databaseProductName;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}