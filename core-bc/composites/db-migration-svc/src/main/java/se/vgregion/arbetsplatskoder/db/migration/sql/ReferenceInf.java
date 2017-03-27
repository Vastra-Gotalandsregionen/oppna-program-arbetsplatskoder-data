package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clalu4 on 2016-12-27.
 */
public class ReferenceInf implements Serializable {

    private List<Mapping> mappings = new ArrayList<>();

    public List<Mapping> getMappings() {
        return mappings;
    }

    private TableInf primaryTable;

    private TableInf foreignTable;

    public TableInf getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(TableInf foreignTable) {
        this.foreignTable = foreignTable;
    }

    public TableInf getPrimaryTable() {
        return primaryTable;
    }

    public void setPrimaryTable(TableInf primaryTable) {
        this.primaryTable = primaryTable;
    }

    public static class Mapping implements Serializable {
        private String pktableCatalog;
        private String pktableSchema;
        private String pktableName;
        private String pkcolumnName;
        private String fktableCatalog;
        private String fktableSchema;
        private String fktableName;
        private String fkcolumnName;
        private Short ordinalPosition;
        private Short updateRule;
        private Short deleteRule;
        private String fkName;
        private String pkName;
        private Short deferrability;

        public String getPktableCatalog() {
            return pktableCatalog;
        }

        public void setPktableCatalog(String pktableCatalog) {
            this.pktableCatalog = pktableCatalog;
        }

        public String getPktableSchema() {
            return pktableSchema;
        }

        public void setPktableSchema(String pktableSchema) {
            this.pktableSchema = pktableSchema;
        }

        public String getPktableName() {
            return pktableName;
        }

        public void setPktableName(String pktableName) {
            this.pktableName = pktableName;
        }

        public String getPkcolumnName() {
            return pkcolumnName;
        }

        public void setPkcolumnName(String pkcolumnName) {
            this.pkcolumnName = pkcolumnName;
        }

        public String getFktableCatalog() {
            return fktableCatalog;
        }

        public void setFktableCatalog(String fktableCatalog) {
            this.fktableCatalog = fktableCatalog;
        }

        public String getFktableSchema() {
            return fktableSchema;
        }

        public void setFktableSchema(String fktableSchema) {
            this.fktableSchema = fktableSchema;
        }

        public String getFktableName() {
            return fktableName;
        }

        public void setFktableName(String fktableName) {
            this.fktableName = fktableName;
        }

        public String getFkcolumnName() {
            return fkcolumnName;
        }

        public void setFkcolumnName(String fkcolumnName) {
            this.fkcolumnName = fkcolumnName;
        }

        public Short getOrdinalPosition() {
            return ordinalPosition;
        }

        public void setOrdinalPosition(Short ordinalPosition) {
            this.ordinalPosition = ordinalPosition;
        }

        public Short getUpdateRule() {
            return updateRule;
        }

        public void setUpdateRule(Short updateRule) {
            this.updateRule = updateRule;
        }

        public Short getDeleteRule() {
            return deleteRule;
        }

        public void setDeleteRule(Short deleteRule) {
            this.deleteRule = deleteRule;
        }

        public String getFkName() {
            return fkName;
        }

        public void setFkName(String fkName) {
            this.fkName = fkName;
        }

        public String getPkName() {
            return pkName;
        }

        public void setPkName(String pkName) {
            this.pkName = pkName;
        }

        public Short getDeferrability() {
            return deferrability;
        }

        public void setDeferrability(Short deferrability) {
            this.deferrability = deferrability;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Mapping that = (Mapping) o;

            if (pktableCatalog != null ? !pktableCatalog.equals(that.pktableCatalog) : that.pktableCatalog != null)
                return false;
            if (pktableSchema != null ? !pktableSchema.equals(that.pktableSchema) : that.pktableSchema != null)
                return false;
            if (pktableName != null ? !pktableName.equals(that.pktableName) : that.pktableName != null) return false;
            if (pkcolumnName != null ? !pkcolumnName.equals(that.pkcolumnName) : that.pkcolumnName != null)
                return false;
            if (fktableCatalog != null ? !fktableCatalog.equals(that.fktableCatalog) : that.fktableCatalog != null)
                return false;
            if (fktableSchema != null ? !fktableSchema.equals(that.fktableSchema) : that.fktableSchema != null)
                return false;
            if (fktableName != null ? !fktableName.equals(that.fktableName) : that.fktableName != null) return false;
            if (fkcolumnName != null ? !fkcolumnName.equals(that.fkcolumnName) : that.fkcolumnName != null)
                return false;
            if (ordinalPosition != null ? !ordinalPosition.equals(that.ordinalPosition) : that.ordinalPosition != null)
                return false;
            if (updateRule != null ? !updateRule.equals(that.updateRule) : that.updateRule != null) return false;
            if (deleteRule != null ? !deleteRule.equals(that.deleteRule) : that.deleteRule != null) return false;
            if (fkName != null ? !fkName.equals(that.fkName) : that.fkName != null) return false;
            if (pkName != null ? !pkName.equals(that.pkName) : that.pkName != null) return false;
            return deferrability != null ? deferrability.equals(that.deferrability) : that.deferrability == null;
        }

        @Override
        public int hashCode() {
            int result = pktableCatalog != null ? pktableCatalog.hashCode() : 0;
            result = 31 * result + (pktableSchema != null ? pktableSchema.hashCode() : 0);
            result = 31 * result + (pktableName != null ? pktableName.hashCode() : 0);
            result = 31 * result + (pkcolumnName != null ? pkcolumnName.hashCode() : 0);
            result = 31 * result + (fktableCatalog != null ? fktableCatalog.hashCode() : 0);
            result = 31 * result + (fktableSchema != null ? fktableSchema.hashCode() : 0);
            result = 31 * result + (fktableName != null ? fktableName.hashCode() : 0);
            result = 31 * result + (fkcolumnName != null ? fkcolumnName.hashCode() : 0);
            result = 31 * result + (ordinalPosition != null ? ordinalPosition.hashCode() : 0);
            result = 31 * result + (updateRule != null ? updateRule.hashCode() : 0);
            result = 31 * result + (deleteRule != null ? deleteRule.hashCode() : 0);
            result = 31 * result + (fkName != null ? fkName.hashCode() : 0);
            result = 31 * result + (pkName != null ? pkName.hashCode() : 0);
            result = 31 * result + (deferrability != null ? deferrability.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Mapping{" +
                    "pktableCatalog='" + pktableCatalog + '\'' +
                    ", pktableSchema='" + pktableSchema + '\'' +
                    ", pktableName='" + pktableName + '\'' +
                    ", pkcolumnName='" + pkcolumnName + '\'' +
                    ", fktableCatalog='" + fktableCatalog + '\'' +
                    ", fktableSchema='" + fktableSchema + '\'' +
                    ", fktableName='" + fktableName + '\'' +
                    ", fkcolumnName='" + fkcolumnName + '\'' +
                    ", ordinalPosition=" + ordinalPosition +
                    ", updateRule=" + updateRule +
                    ", deleteRule=" + deleteRule +
                    ", fkName='" + fkName + '\'' +
                    ", pkName='" + pkName + '\'' +
                    ", deferrability=" + deferrability +
                    '}';
        }
    }

    public static List<ReferenceInf> toReferenceInfs(List<Mapping> mappings) {
        List<ReferenceInf> result = new ArrayList<>();
        ReferenceInf current = null;
        Short first = new Short((short) 1);
        for (Mapping mapping : mappings) {
            if (mapping.getOrdinalPosition().equals(first)) {
                current = new ReferenceInf();
                result.add(current);
            }
            current.getMappings().add(mapping);
        }
        return result;
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

        ReferenceInf that = (ReferenceInf) o;

        return mappings != null ? mappings.equals(that.mappings) : that.mappings == null;
    }

    @Override
    public int hashCode() {
        return mappings != null ? mappings.hashCode() : 0;
    }

}
