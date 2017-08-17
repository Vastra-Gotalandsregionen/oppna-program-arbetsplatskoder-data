package se.vgregion.arbetsplatskoder.db.migration.sql.meta;

import java.io.Serializable;

public abstract class AbstractPortedKey implements Serializable {

  protected String pktableCat;
  protected String pktableSchem;
  protected String pktableName;
  protected String pkcolumnName;
  protected String fktableCat;
  protected String fktableSchem;
  protected String fktableName;
  protected String fkcolumnName;
  protected Integer keySeq;
  protected Integer updateRule;
  protected Integer deleteRule;
  protected String fkName;
  protected String pkName;
  protected Integer deferrability;

  public String getPktableCat() {
    return pktableCat;
  }

  public void setPktableCat(String pktableCat) {
    this.pktableCat = pktableCat;
  }

  public String getPktableCatalog() {
    return pktableCat;
  }

  public void setPktableCatalog(String pktableCat) {
    this.pktableCat = pktableCat;
  }

  public String getPktableSchem() {
    return pktableSchem;
  }

  public void setPktableSchem(String pktableSchem) {
    this.pktableSchem = pktableSchem;
  }

  public String getPktableSchema() {
    return pktableSchem;
  }

  public void setPktableSchema(String pktableSchem) {
    this.pktableSchem = pktableSchem;
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
    return fktableCat;
  }

  public void setFktableCatalog(String fktableCat) {
    this.fktableCat = fktableCat;
  }

  public String getFktableCat() {
    return fktableCat;
  }

  public void setFktableCat(String fktableCat) {
    this.fktableCat = fktableCat;
  }

  public String getFktableSchema() {
    return fktableSchem;
  }

  public void setFktableSchema(String fktableSchem) {
    this.fktableSchem = fktableSchem;
  }

  public String getFktableSchem() {
    return fktableSchem;
  }

  public void setFktableSchem(String fktableSchem) {
    this.fktableSchem = fktableSchem;
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

  /**
   * Simulates the property ordinal_position that is returned by H2-database engine, instead of the stipulated one
   * key_seq.
   * @return value of keySeq.
   */
  public Integer getOrdinalPosition() {
    return keySeq;
  }

  /**
   * Se se.bean.reactor.sql.PortedKeyInf#getOrdinalPosition() for general explanation.
   * @param keySeq sets the property keySeq.
   */
  public void setOrdinalPosition(Integer keySeq) {
    this.keySeq = keySeq;
  }

  public Integer getKeySeq() {
    return keySeq;
  }

  public void setKeySeq(Integer keySeq) {
    this.keySeq = keySeq;
  }

  public Integer getUpdateRule() {
    return updateRule;
  }

  public void setUpdateRule(Integer updateRule) {
    this.updateRule = updateRule;
  }

  public Integer getDeleteRule() {
    return deleteRule;
  }

  public void setDeleteRule(Integer deleteRule) {
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

  public Integer getDeferrability() {
    return deferrability;
  }

  public void setDeferrability(Integer deferrability) {
    this.deferrability = deferrability;
  }



}