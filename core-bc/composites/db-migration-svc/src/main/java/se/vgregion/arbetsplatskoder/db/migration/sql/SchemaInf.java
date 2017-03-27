package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clalu4 on 2016-12-27.
 */
public class SchemaInf implements Serializable {

    private String name;

    private List<TableInf> tables = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TableInf> getTables() {
        return tables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SchemaInf schemaInf = (SchemaInf) o;

        if (name != null ? !name.equals(schemaInf.name) : schemaInf.name != null) return false;
        return tables != null ? tables.equals(schemaInf.tables) : schemaInf.tables == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (tables != null ? tables.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SchemaInf{" +
                "name='" + name + '\'' +
                ", tables=" + tables +
                '}';
    }

    public static List<SchemaInf> toSchemas(List<TableInf> tables) {
        List<SchemaInf> result = new ArrayList<>();
        String name = "fooBarBaz123 :*)";
        SchemaInf current = null;
        for (TableInf table : tables) {
            if (!name.equals(table.getTableSchema())) {
                current = new SchemaInf();
                name = table.getTableSchema();
                current.setName(name);
                result.add(current);
            }
            current.getTables().add(table);
        }
        return result;
    }

    public TableInf getTable(String byItsName) {
        for (TableInf table : tables) {
            if (table.getTableName().equals(byItsName)) {
                return table;
            }
        }
        return null;
    }
}
