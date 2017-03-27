package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clalu4 on 2016-12-27.
 */
public class TableInf implements Serializable {

    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String tableType;
    private String remarks;
    private String typeName;
    private String sql;

    private List<ReferenceInf> references = new ArrayList<>();

    private List<ColumnInf> columns = new ArrayList<>();

    private List<PrimaryKeyInf> primaryKeys = new ArrayList<>();

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableInf tableInf = (TableInf) o;

        if (tableCatalog != null ? !tableCatalog.equals(tableInf.tableCatalog) : tableInf.tableCatalog != null)
            return false;
        if (tableSchema != null ? !tableSchema.equals(tableInf.tableSchema) : tableInf.tableSchema != null)
            return false;
        if (tableName != null ? !tableName.equals(tableInf.tableName) : tableInf.tableName != null) return false;
        if (tableType != null ? !tableType.equals(tableInf.tableType) : tableInf.tableType != null) return false;
        if (remarks != null ? !remarks.equals(tableInf.remarks) : tableInf.remarks != null) return false;
        if (typeName != null ? !typeName.equals(tableInf.typeName) : tableInf.typeName != null) return false;
        return sql != null ? sql.equals(tableInf.sql) : tableInf.sql == null;
    }

    @Override
    public int hashCode() {
        int result = tableCatalog != null ? tableCatalog.hashCode() : 0;
        result = 31 * result + (tableSchema != null ? tableSchema.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (tableType != null ? tableType.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (sql != null ? sql.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TableInf{" +
                "tableCatalog='" + tableCatalog + '\'' +
                ", tableSchema='" + tableSchema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableType='" + tableType + '\'' +
                ", remarks='" + remarks + '\'' +
                ", typeName='" + typeName + '\'' +
                //", sql='" + sql + '\'' +
                "}\n";
    }

    public List<ReferenceInf> getReferences() {
        return references;
    }

    public List<ColumnInf> getColumns() {
        return columns;
    }

    public List<PrimaryKeyInf> getPrimaryKeys() {
        return primaryKeys;
    }

    public String getTableSchem() {
        return tableSchema;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchema = tableSchem;
    }
}
