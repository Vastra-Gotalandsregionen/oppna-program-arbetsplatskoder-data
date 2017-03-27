package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.io.Serializable;

/**
 * Created by clalu4 on 2016-12-27.
 */
public class ColumnInf implements Serializable {

    private String tableName;
    private String columnName;
    private String columnClassName;
    private int precision;
    private int columnDisplaySize;
    private int columnType;
    private int scale;
    private String columnLabel;
    private String schemaName;
    private String catalogName;
    private String columnTypeName;
    private boolean primary;
    private boolean nullable;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnClassName() {
        return columnClassName;
    }

    public void setColumnClassName(String columnClassName) {
        this.columnClassName = columnClassName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public void setColumnDisplaySize(int columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    @Override
    public String toString() {
        return "ColumnInf{" +
                "tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnClassName='" + columnClassName + '\'' +
                ", precision=" + precision +
                ", columnDisplaySize=" + columnDisplaySize +
                ", columnType=" + columnType +
                ", scale=" + scale +
                ", columnLabel='" + columnLabel + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", catalogName='" + catalogName + '\'' +
                ", columnTypeName='" + columnTypeName + '\'' +
                ", primary=" + primary +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnInf columnInf = (ColumnInf) o;

        if (precision != columnInf.precision) return false;
        if (columnDisplaySize != columnInf.columnDisplaySize) return false;
        if (columnType != columnInf.columnType) return false;
        if (scale != columnInf.scale) return false;
        if (primary != columnInf.primary) return false;
        if (tableName != null ? !tableName.equals(columnInf.tableName) : columnInf.tableName != null) return false;
        if (columnName != null ? !columnName.equals(columnInf.columnName) : columnInf.columnName != null) return false;
        if (columnClassName != null ? !columnClassName.equals(columnInf.columnClassName) : columnInf.columnClassName != null)
            return false;
        if (columnLabel != null ? !columnLabel.equals(columnInf.columnLabel) : columnInf.columnLabel != null)
            return false;
        if (schemaName != null ? !schemaName.equals(columnInf.schemaName) : columnInf.schemaName != null) return false;
        if (catalogName != null ? !catalogName.equals(columnInf.catalogName) : columnInf.catalogName != null)
            return false;
        return columnTypeName != null ? columnTypeName.equals(columnInf.columnTypeName) : columnInf.columnTypeName == null;
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 31 * result + (columnClassName != null ? columnClassName.hashCode() : 0);
        result = 31 * result + precision;
        result = 31 * result + columnDisplaySize;
        result = 31 * result + columnType;
        result = 31 * result + scale;
        result = 31 * result + (columnLabel != null ? columnLabel.hashCode() : 0);
        result = 31 * result + (schemaName != null ? schemaName.hashCode() : 0);
        result = 31 * result + (catalogName != null ? catalogName.hashCode() : 0);
        result = 31 * result + (columnTypeName != null ? columnTypeName.hashCode() : 0);
        result = 31 * result + (primary ? 1 : 0);
        return result;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
