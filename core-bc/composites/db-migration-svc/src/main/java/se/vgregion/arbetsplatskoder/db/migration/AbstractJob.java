package se.vgregion.arbetsplatskoder.db.migration;

import se.vgregion.arbetsplatskoder.db.migration.sql.*;
import se.vgregion.arbetsplatskoder.db.migration.util.Filez;
import se.vgregion.arbetsplatskoder.db.migration.util.Zerial;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by clalu4 on 2017-03-22.
 */
public abstract class AbstractJob {

    protected ConnectionExt legacyCon;
    protected ConnectionExt mainCon;

    private Properties getLegacyProperties() {
        Path path = (Paths.get(System.getProperty("user.home"), ".hotell", "arbetsplatskoder", "legacy.jdbc.properties"));
        return getProperties(path);
    }

    private Properties getMainJdbcProperties() {
        Path path = (Paths.get(System.getProperty("user.home"), ".hotell", "arbetsplatskoder", "main.jdbc.properties"));
        return getProperties(path);
    }

    private Properties getProperties(Path fromHere) {
        Properties properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(fromHere)) {
            properties.load(reader);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return properties;
    }

    public ConnectionExt getLegacyConnectionExt() {
//        Console console = System.console();

//        char[] chars = console.readPassword("Password?");
//        String password = new String(chars);

        Properties prop = getLegacyProperties();
        ConnectionExt connection = new ConnectionExt(
                prop.getProperty("url"),
                prop.getProperty("user"),
                prop.getProperty("password"),
//                password,
                prop.getProperty("driver"));
        return connection;
    }

    public ConnectionExt getMainConnectionExt() {
        Properties prop = getMainJdbcProperties();
        ConnectionExt connection = new ConnectionExt(
                prop.getProperty("url"),
                prop.getProperty("user"),
                prop.getProperty("password"),
                prop.getProperty("driver"));
        return connection;
    }

    public void init() {
        legacyCon = getLegacyConnectionExt();
        mainCon = getMainConnectionExt();
    }

    public void copyTypesFromLegacyToDiscCache() {
        for (TableInf table : legacyCon.getSchemas("dbo").get(0).getTables()) {
            Path path = Paths.get(getTypesCacheDirectory().toString(), table.getTableName() + ".table");
            Zerial.toFile(table, path);
        }
    }

    public void dropTablesAlreadyInMain() {
        ConnectionExt local = getMainConnectionExt();
        for (TableInf table : getTablesOnDisc()) {
            local.execute("drop table if exists " + table.getTableName());
            local.commit();
            System.out.println("Droped " + table.getTableName());
        }
    }

    public void createTablesInMainDatabase() {
        mainCon.getSchemas("public");
        for (TableInf table : getTablesOnDisc()) {
            mainCon.execute(toDdl(table));
        }
        mainCon.commit();
    }


    public void main() {
        ConnectionExt connection = getMainConnectionExt();
        long timeBefore = System.currentTimeMillis();
        for (SchemaInf schema : connection.getSchemas("dbo")) {
            Path path = Paths.get(System.getProperty("user.home"), "temp", "Apk", "Schemas", schema.getName() + ".java.obj");
            Zerial.toFile(schema, path);
        }
        System.out.println("Time for getting the schema where " + Math.round(System.currentTimeMillis() - timeBefore) / 1000);
    }


    public void copyDataToDiscCache() {
        //SchemaInf dbo = legacyCon.getSchemas("dbo").get(0);
        for (TableInf table : getTablesOnDisc()) {
            System.out.println("Fetching data from " + table.getTableName());
            List<Map<String, Object>> tupels = legacyCon.query("select * from dbo." + table.getTableName());
            Path path = Paths.get(getDataCacheDirectory().toString(), table.getTableName() + ".java.obj");
            Zerial.toFile(tupels, path);
        }
    }

    public List<TableInf> getTablesOnDisc() {
        List<TableInf> result = new ArrayList<>();
        Path path = getTypesCacheDirectory();
        File[] schemaFiles = path.toFile().listFiles();
        for (File schemaFile : schemaFiles) {
            result.add(Zerial.fromFile(TableInf.class, schemaFile.toPath()));
        }
        return result;
    }

    public void copyDataIntoMainDatabase() {
        for (TableInf table : getTablesOnDisc()) {
            try {
                System.out.println("Inserts data into " + table.getTableName());
//                Path path = Paths.get(System.getProperty("user.home"), "temp", "Apk", "Data", table.getTableName() + ".java.obj");
                Path path = Paths.get(getDataCacheDirectory().toString(), table.getTableName() + ".java.obj");
                List<Map<String, Object>> items = Zerial.fromFile(List.class, path);
                for (Map<String, Object> item : items) {
                    mainCon.insert(table.getTableName(), item);
                }
                mainCon.commit();
            } catch (Exception e) {
                System.out.println("Failed insert into " + table);
                throw new RuntimeException(e);
            }
        }
    }

    public void findTableAndColumnNamesInsideFiles() {
        Path path = (Paths.get(System.getProperty("user.home"), ".hotell", "arbetsplatskoder", "legacy.jdbc.properties"));
        Properties properties = getProperties(path);
        if (properties.containsKey("old.code")) {
            findTableAndColumnNamesInsideFiles(Paths.get(properties.getProperty("old.code")));
        } else {
            System.out.println("The file " + path + " does not contain the 'old.code' property. Skips this.");
        }
    }

    public void findTableAndColumnNamesInsideFiles(Path dirOfTheFiles) {
        Path root = dirOfTheFiles;
        //System.out.println(Filez.findDistinctWords(root, "AgarformID"));
        StringBuilder sb = new StringBuilder("Typ;Nyckelord;Förekomster\n");
        for (TableInf table : getTablesOnDisc()) {
            sb.append("Tabell;" + table.getTableName() + ";" + Filez.findDistinctWords(root, table.getTableName()).size() + "\n");
            for (ColumnInf column : table.getColumns()) {
                sb.append("Column;" + column.getColumnName() + ";" + Filez.findDistinctWords(root, column.getColumnName()).size() + "\n");
                Filez.findDistinctWords(root, column.getColumnName());
            }
            sb.append("---;---;---\n");
        }
        System.out.println(sb);
    }

    public String toDdl(TableInf table) {
        StringBuilder sb = new StringBuilder("create table " + table.getTableName());

        Junctor<Atom> types = new Junctor("(", ", ", ")");

        Properties translations = getTypeTranslations();

        Set<String> textFormatsWithNoParm = new HashSet<>(Arrays.asList("text"));

        for (ColumnInf column : table.getColumns()) {
            String originalType = column.getColumnTypeName();
            if (originalType.endsWith(" identity")) {
                originalType = originalType.replace(" identity", "");
            }
            String type = translations.getProperty(originalType, originalType);

            if (!textFormatsWithNoParm.contains(type))
                if (column.getColumnClassName().equals(String.class.getName())) {
                    type += "(" + column.getColumnDisplaySize() + ")";
                }
            if (column.isNullable())
                types.add(new Atom<>(column.getColumnName() + " " + type));
            else
                types.add(new Atom<>(column.getColumnName() + " " + type + " not null"));
        }

        // CONSTRAINT user_pkey PRIMARY KEY (id)

        if (!table.getPrimaryKeys().isEmpty()) {
            Junctor<Atom> pks = new Junctor<>("(", ", ", ")");
            table.getPrimaryKeys().forEach(pk -> {
                pks.add(new Atom(pk.getColumnName()));
            });
            StringBuilder pkSb = new StringBuilder();
            pks.toSql(pkSb, new ArrayList());
            types.add(new Atom("constraint " + table.getTableName() + "_pk primary key" + pkSb));
        }

        types.toSql(sb, new ArrayList());

        return sb.toString();
    }


    public String toJavaEntityClass(TableInf table) {
        StringBuilder sb = new StringBuilder();

        sb.append("package se.vgregion.arbetsplatskoder.domain;\n");

        sb.append("\nimport javax.persistence.*;\n");
        sb.append("\nimport java.io.Serializable;\n");

        sb.append("\n@Entity");
        sb.append("\n@Table(name = \"ROOM\")".replace("ROOM", table.getTableName()));
        sb.append("\npublic class " + JdbcUtil.toProperCase(JdbcUtil.toCamelCase(table.getTableName()))
                + " extends AbstractEntity {\n");

        boolean hasId = hasPrimaryKeyAtAll(table);

        if (!hasId) {
            sb.append(
                    "    @Id\n" +
                            "    @GeneratedValue(strategy = GenerationType.AUTO)\n" +
                            "    private Long id;");
        }

        for (ColumnInf column : table.getColumns()) {
            if (column.isPrimary() || column.getColumnName().equalsIgnoreCase("id")) {
                sb.append("\n    @Id");
            }
            sb.append("\n    " + toJavaColumnAnnotationCode(column));
            sb.append("\n    private " + getColumnJavaCodeType(column) + " " + JdbcUtil.toCamelCase(column.getColumnName()) + ";\n");
        }

        if (!hasId) {
            sb.append(
                    "    public Long getId() {\n" +
                            "        return id;\n" +
                            "    }\n" +
                            "\n" +
                            "    public void setId(Long id) {\n" +
                            "        this.id = id;\n" +
                            "    }");
        }

        for (ColumnInf column : table.getColumns()) {
            sb.append(toGetterCode(column));
            sb.append(toSetterCode(column));
        }

        sb.append("\n\n}");

        return sb.toString();
    }

    private String toGetterCode(ColumnInf column) {
        StringBuilder sb = new StringBuilder("");
        String property = JdbcUtil.toCamelCase(column.getColumnName());
        sb.append("\n    public " + getColumnJavaCodeType(column) + " ");
        sb.append("get" + JdbcUtil.toProperCase(property) + "(){\n");
        sb.append("        return " + property + ";");
        sb.append("\n    }\n");
        return sb.toString();
    }

    private String toSetterCode(ColumnInf column) {
        StringBuilder sb = new StringBuilder("");
        String property = JdbcUtil.toCamelCase(column.getColumnName());
        sb.append("\n    public void ");
        sb.append("set" + JdbcUtil.toProperCase(property) + "(" + getColumnJavaCodeType(column) + " v){\n");
        sb.append("        this." + property + " = v;");
        sb.append("\n    }\n");
        return sb.toString();
    }

    private String getColumnJavaCodeType(ColumnInf column) {
        String name = column.getColumnClassName();
        if (name.startsWith("[")) {
            if (name.startsWith("[B")) {
                return "Byte[]";
            } else {
                throw new RuntimeException("Unknown array type: " + name);
            }
        } else {
            return name;
        }
    }

    private String quote(Object s) {
        return '"' + s.toString() + '"';
    }

    public String toJavaColumnAnnotationCode(ColumnInf column) {
        StringBuilder sb = new StringBuilder();
        Junctor<Match> matches = new Junctor<>("@Column (", ", ", ")");
        matches.add(new Match(new Atom("name"), " = ", new Atom(quote(column.getColumnName()))));
        matches.add(new Match(new Atom("nullable"), " = ", new Atom(column.isNullable())));

        String type = column.getColumnTypeName();
        if (!type.equals("text") && column.getColumnClassName().equals(String.class.getName()) && column.getColumnDisplaySize() > 0) {
            matches.add(new Match(new Atom("length"), " = ", new Atom(column.getColumnDisplaySize())));
        }
        matches.toSql(sb, new ArrayList());
        return sb.toString();
    }


    public Properties getTypeTranslations() {
        try {
            URL url = this.getClass().getResource("SQLServer-PostgreSQL-DataTypes.txt");
            List<String> rows = Files.readAllLines(Paths.get(url.getFile().substring(0)));

            Properties result = new Properties();

            for (String row : rows) {
                String[] pair = row.split(Pattern.quote(";"));
                pair[0] = pair[0].replaceAll("\\(.*\\)", "");
                pair[1] = pair[1].replaceAll("\\(.*\\)", "");
                result.put(pair[0], pair[1]);
                result.put(pair[0].toLowerCase(), pair[1].toLowerCase());
                result.put(pair[0].toUpperCase(), pair[1].toLowerCase());
            }

            return result;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    String getDatabaseCacheDirectory() {
        String path = new File(".").getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        Path p = Paths.get(path, "database-cache");
        createDirIfNotThere(p);
        return p.toString();
    }

    Path getDataCacheDirectory() {
        Path path = Paths.get(getDatabaseCacheDirectory(), "data");
        createDirIfNotThere(path);
        return path;
    }

    Path getTypesCacheDirectory() {
        Path path = Paths.get(getDatabaseCacheDirectory(), "types");
        createDirIfNotThere(path);
        return path;
    }

    private void createDirIfNotThere(Path p) {
        if (!Files.exists(p)) {
            try {
                System.out.println("Creates " + p);
                Files.createDirectories(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean hasPrimaryKeyAtAll(TableInf table) {
        for (ColumnInf column : table.getColumns()) {
            if (column.isPrimary() || column.getColumnName().equalsIgnoreCase("id")) {
                return true;
            }
        }
        return false;
    }

    public void createEntitySourceFiles() {
        String projectDir = new File(".").getAbsolutePath();
        projectDir = projectDir.substring(0, projectDir.length() - 2);
        System.out.println(projectDir);
        Path classDest = Paths.get(projectDir, "core-bc", "composites", "types", "src", "main", "java", "se", "vgregion", "arbetsplatskoder", "domain");

        for (String s : classDest.toFile().list()) {
            System.out.println(s);
        }
        try {
            for (TableInf table : mainCon.getSchemas().get(0).getTables()) {
                String javaCode = toJavaEntityClass(table);
                Files.write(
                        Paths.get(classDest.toString(), JdbcUtil.toProperCase(JdbcUtil.toCamelCase(table.getTableName())) + ".java"),
                        javaCode.getBytes()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
