package se.vgregion.arbetsplatskoder.db.migration.sql.meta;

import java.io.Serializable;

/**
 * Created by clalu4 on 2016-12-27.
 */
public class Primary implements Serializable {

    private String tableCat, tableSchem, tableName, columnName, pkName;
    private Short keySeq, ordinalPosition /*This one is not in the spec*/;

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    /*This one is just fo h2 db*/
    public String getTableCatalog() {
        return tableCat;
    }

    /*This one is just fo h2 db*/
    public void setTableCatalog(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    /*This one is just fo h2 db*/
    public String getTableSchema() {
        return tableSchem;
    }

    /*This one is just fo h2 db*/
    public void setTableSchema(String tableSchem) {
        this.tableSchem = tableSchem;
    }

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

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public Short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(Short keySeq) {
        this.keySeq = keySeq;
    }

    public Short getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Short ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }
}
