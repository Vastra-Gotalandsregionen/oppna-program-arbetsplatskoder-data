package se.vgregion.arbetsplatskoder.db.migration.sql;

import se.vgregion.arbetsplatskoder.db.migration.util.ObjectUtil;

import java.sql.*;
import java.util.*;

public class ConnectionExt {

    private String user, password, url, driver;

    private Connection connection = null;
    private PreparedStatement currentRun;

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

    public List<Map<String, Object>> query(String question, Object... withParameters) {
        try {
            return JdbcUtil.toMaps(getResultSetByQuery(question, withParameters));
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

    /*
    public List<Map<String, Object>> query(Select select) {
        StringBuilder sb = new StringBuilder();
        List values = new ArrayList();
        select.toSql(sb, values);
        return query(sb.toString(), values.toArray(new Object[values.size()]));
    }*/

    public List<Map<String, Object>> query(String question, final int start, final int end, Object... withParameters) {
        try {
            return JdbcUtil.toMaps(getResultSetByQuery(question, withParameters), start, end);
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
            ps = connection.prepareCall(sql);
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
            ps = connection.prepareCall(script);
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

    public void insert(String intoTable, Map<String, Object> tupel) {
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

            ps.executeUpdate();
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

    public List<SchemaInf> getSchemas() {
        try {
            return SchemaInf.toSchemas(getAllTables());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SchemaInf> getSchemas(String withSchemaPattern) {
        try {
            return SchemaInf.toSchemas(getAllTablesImpl(withSchemaPattern));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TableInf> getAllTables() {
        try {
            return getAllTablesImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<TableInf> getAllTablesImpl() throws SQLException {
        return getAllTablesImpl(null);
    }

    private List<TableInf> getAllTablesImpl(String withSchemaName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet rs = meta.getTables(
                null,
                withSchemaName,
                null,
                new String[]{"TABLE", "VIEW"});
        List<TableInf> result = JdbcUtil.toBeans(rs, TableInf.class);

        for (TableInf t1 : new ArrayList<>(result)) {
            if (!ObjectUtil.isEmptyOrNull(withSchemaName)) {
                if (withSchemaName.equalsIgnoreCase(t1.getTableSchema()) || t1.getTableSchema().matches(withSchemaName)) {
                    // Do nothing.
                } else {
                    // Skip this.
                    result.remove(t1);
                    continue;
                }
            }
            System.out.println("Examining table " + t1.getTableSchema() + "." + t1.getTableName());

            rs = meta.getPrimaryKeys(t1.getTableCatalog(), t1.getTableSchema(), t1.getTableName());
            t1.getPrimaryKeys().addAll(JdbcUtil.toBeans(rs, PrimaryKeyInf.class));
            Set<String> prims = new HashSet<>();
            for (PrimaryKeyInf prim : t1.getPrimaryKeys()) {
                prims.add(prim.getColumnName());
            }

            CallableStatement ps = connection.prepareCall(
                    "select * from " + t1.getTableSchema() + "." + t1.getTableName());
            rs = ps.executeQuery();
            t1.getColumns().addAll(JdbcUtil.toColumnInfs(rs));
            rs.close();
            ps.close();

            for (ColumnInf column : t1.getColumns()) {
                column.setPrimary(prims.contains(column.getColumnName()));
            }

            for (TableInf t2 : result) {
                rs = meta.getCrossReference(t1.getTableCatalog(), t1.getTableSchema(), t1.getTableName(),
                        t2.getTableCatalog(), t2.getTableSchema(), t2.getTableName());
                List<ReferenceInf> primary = ReferenceInf.toReferenceInfs(JdbcUtil.toBeans(rs, ReferenceInf.Mapping.class));

                if (primary.isEmpty()) {
                    List<Map<String, Object>> exported = JdbcUtil.toMaps(meta.getExportedKeys(t2.getTableCatalog(), t2.getTableSchema(), t2.getTableName()));
                    List<Map<String, Object>> imported = JdbcUtil.toMaps(meta.getImportedKeys(t2.getTableCatalog(), t2.getTableSchema(), t2.getTableName()));
                    if (!exported.isEmpty() || !imported.isEmpty()) {
                        System.out.println("Did not get any cross refs for " + t2.getTableName());
                        System.out.println("Found Exported " + exported);
                        System.out.println("Found Imported " + imported);
                    }
                }

                for (ReferenceInf p : primary) {
                    p.setPrimaryTable(t1);
                    p.setForeignTable(t2);
                }
                t1.getReferences().addAll(primary);

                rs = meta.getCrossReference(t2.getTableCatalog(), t2.getTableSchema(), t2.getTableName(),
                        t1.getTableCatalog(), t1.getTableSchema(), t1.getTableName());
                List<ReferenceInf> foreign = ReferenceInf.toReferenceInfs(JdbcUtil.toBeans(rs, ReferenceInf.Mapping.class));
                for (ReferenceInf f : foreign) {
                    f.setForeignTable(t1);
                    f.setPrimaryTable(t2);
                }
                t1.getReferences().addAll(foreign);
            }
        }

        return result;
    }

}