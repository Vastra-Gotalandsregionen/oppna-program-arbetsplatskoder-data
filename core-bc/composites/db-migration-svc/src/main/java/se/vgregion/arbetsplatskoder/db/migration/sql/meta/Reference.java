package se.vgregion.arbetsplatskoder.db.migration.sql.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clalu4 on 2016-12-27.
 */
public class Reference extends AbstractPortedKey implements Serializable {

    private List<Mapping> mappings = new ArrayList<>();

    public List<Mapping> getMappings() {
        return mappings;
    }

    private Table primaryTable;

    private Table foreignTable;

    public Table getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(Table foreignTable) {
        this.foreignTable = foreignTable;
    }

    public Table getPrimaryTable() {
        return primaryTable;
    }

    public void setPrimaryTable(Table primaryTable) {
        this.primaryTable = primaryTable;
    }

    public static class Mapping extends AbstractPortedKey {

    }

    @Override
    public String toString() {
        return "ReferenceInf{" +
                "mappings=" + mappings +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reference that = (Reference) o;

        return mappings != null ? mappings.equals(that.mappings) : that.mappings == null;
    }

    @Override
    public int hashCode() {
        return mappings != null ? mappings.hashCode() : 0;
    }

}
