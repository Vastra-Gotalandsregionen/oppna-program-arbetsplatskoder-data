package se.vgregion.arbetsplatskoder.db.migration.sql.meta;

import java.io.Serializable;

/**
 * Created by clalu4 on 2017-06-09.
 */
public class Privilege implements Serializable {
  private String tableCatalog;
  private String tableSchema;
  private String tableName;
  private String grantor;
  private String grantee;
  private String privilegeType;
  private String isGrantable;

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

  public String getGrantor() {
    return grantor;
  }

  public void setGrantor(String grantor) {
    this.grantor = grantor;
  }

  public String getGrantee() {
    return grantee;
  }

  public void setGrantee(String grantee) {
    this.grantee = grantee;
  }

  public String getPrivilegeType() {
    return privilegeType;
  }

  public void setPrivilegeType(String privilegeType) {
    this.privilegeType = privilegeType;
  }

  public String getPrivilege() {
    return privilegeType;
  }

  public void setPrivilege(String privilegeType) {
    this.privilegeType = privilegeType;
  }

  public String getIsGrantable() {
    return isGrantable;
  }

  public void setIsGrantable(String isGrantable) {
    this.isGrantable = isGrantable;
  }
}
