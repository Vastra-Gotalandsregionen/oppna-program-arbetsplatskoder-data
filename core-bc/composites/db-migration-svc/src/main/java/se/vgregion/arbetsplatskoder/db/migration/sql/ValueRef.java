package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.util.List;

public class ValueRef implements ProducesSql {


  private final Object var;

  public ValueRef(Object value) {
    super();
    var = value;
  }

  @Override
  public void toSql(StringBuilder sb, List values) {
    sb.append("?");
    values.add(var);
  }

  public Object getVar() {
    return var;
  }

}