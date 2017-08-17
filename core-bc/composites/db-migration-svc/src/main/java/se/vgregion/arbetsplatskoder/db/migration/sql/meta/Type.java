package se.vgregion.arbetsplatskoder.db.migration.sql.meta;

import java.io.Serializable;

/**
 * Created by clalu4 on 2017-06-20.
 */
public class Type implements Serializable {

  private String likableColumnSqlFragment;

  private String typeName;
  private Integer dataType;
  private Integer precision;
  private String prefix;
  private String suffix;
  private String params;
  private Short nullable;
  private Boolean caseSensitive;
  private Short searchable;
  private Boolean unsignedAttribute;
  private Boolean fixedPrecScale;
  private Boolean autoIncrement;
  private Short minimumScale;
  private Short maximumScale;
  private Integer sqlDatetimeSub;
  private Integer radix;

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public Integer getDataType() {
    return dataType;
  }

  public void setDataType(Integer dataType) {
    this.dataType = dataType;
  }

  public Integer getPrecision() {
    return precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public Short getNullable() {
    return nullable;
  }

  public void setNullable(Short nullable) {
    this.nullable = nullable;
  }

  public Boolean getCaseSensitive() {
    return caseSensitive;
  }

  public void setCaseSensitive(Boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  public Short getSearchable() {
    return searchable;
  }

  public void setSearchable(Short searchable) {
    this.searchable = searchable;
  }

  public Boolean getUnsignedAttribute() {
    return unsignedAttribute;
  }

  public void setUnsignedAttribute(Boolean unsignedAttribute) {
    this.unsignedAttribute = unsignedAttribute;
  }

  public Boolean getFixedPrecScale() {
    return fixedPrecScale;
  }

  public void setFixedPrecScale(Boolean fixedPrecScale) {
    this.fixedPrecScale = fixedPrecScale;
  }

  public Boolean getAutoIncrement() {
    return autoIncrement;
  }

  public void setAutoIncrement(Boolean autoIncrement) {
    this.autoIncrement = autoIncrement;
  }

  public Short getMinimumScale() {
    return minimumScale;
  }

  public void setMinimumScale(Short minimumScale) {
    this.minimumScale = minimumScale;
  }

  public Short getMaximumScale() {
    return maximumScale;
  }

  public void setMaximumScale(Short maximumScale) {
    this.maximumScale = maximumScale;
  }

  public Integer getSqlDatetimeSub() {
    return sqlDatetimeSub;
  }

  public void setSqlDatetimeSub(Integer sqlDatetimeSub) {
    this.sqlDatetimeSub = sqlDatetimeSub;
  }

  public Integer getRadix() {
    return radix;
  }

  public void setRadix(Integer radix) {
    this.radix = radix;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Type{");
    sb.append("typeName='").append(typeName).append('\'');
    sb.append(", dataType=").append(dataType);
    sb.append(", precision=").append(precision);
    sb.append(", prefix='").append(prefix).append('\'');
    sb.append(", suffix='").append(suffix).append('\'');
    sb.append(", params='").append(params).append('\'');
    sb.append(", nullable=").append(nullable);
    sb.append(", caseSensitive=").append(caseSensitive);
    sb.append(", searchable=").append(searchable);
    sb.append(", unsignedAttribute=").append(unsignedAttribute);
    sb.append(", fixedPrecScale=").append(fixedPrecScale);
    sb.append(", autoIncrement=").append(autoIncrement);
    sb.append(", minimumScale=").append(minimumScale);
    sb.append(", maximumScale=").append(maximumScale);
    sb.append(", sqlDatetimeSub=").append(sqlDatetimeSub);
    sb.append(", radix=").append(radix);
    sb.append('}');
    return sb.toString();
  }

  public String getLikableColumnSqlFragment() {
    return likableColumnSqlFragment;
  }

  public void setLikableColumnSqlFragment(String likableColumnSqlFragment) {
    this.likableColumnSqlFragment = likableColumnSqlFragment;
  }
}
