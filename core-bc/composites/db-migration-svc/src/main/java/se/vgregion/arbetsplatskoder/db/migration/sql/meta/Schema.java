package se.vgregion.arbetsplatskoder.db.migration.sql.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clalu4 on 2016-12-27.
 */
public class Schema implements Serializable {

    private String name;

    private List<Table> tables = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Table> getTables() {
        return tables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schema schema = (Schema) o;

        if (name != null ? !name.equals(schema.name) : schema.name != null) return false;
        return tables != null ? tables.equals(schema.tables) : schema.tables == null;
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

    public static List<Schema> toSchemas(List<Table> tables) {
        List<Schema> result = new ArrayList<>();
        String name = "fooBarBaz123 :*)";
        Schema current = null;
        for (Table table : tables) {
            if (!name.equals(table.getTableSchema())) {
                current = new Schema();
                name = table.getTableSchema();
                current.setName(name);
                result.add(current);
            }
            current.getTables().add(table);
        }
        return result;
    }

    public Table getTable(String byItsName) {
        for (Table table : tables) {
            if (table.getTableName().equals(byItsName)) {
                return table;
            }
        }
        return null;
    }
}
