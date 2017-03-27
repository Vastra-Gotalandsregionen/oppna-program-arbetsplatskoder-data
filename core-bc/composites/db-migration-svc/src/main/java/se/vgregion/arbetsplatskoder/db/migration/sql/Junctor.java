package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clalu4 on 2016-12-16.
 */
public class Junctor<T extends ProducesSql> extends ArrayList<T> implements ProducesSql {

    private String infix = ", ";

    private String begin = "";

    private String end = "";

    public Junctor() {
        super();
    }

    public Junctor(String infix) {
        super();
        this.infix = infix;
    }

    public Junctor(String begin, String infix, String end) {
        super();
        this.begin = begin;
        this.infix = infix;
        this.end = end;
    }

    @Override
    public void toSql(StringBuilder sb, List values) {
        if (!isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (ProducesSql producesSql : this) {
                producesSql.toSql(builder, values);
                builder.append(infix);
            }
            builder = builder.delete(builder.length() - infix.length(), builder.length());
            if (!builder.toString().trim().equals("")) {
                sb.append(begin);
                sb.append(builder);
                sb.append(end);
            }
        }
    }

    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public static Junctor or() {
        Junctor result = new Junctor(" or ");
        result.setBegin("(");
        result.setEnd(")");
        return result;
    }

    public static Junctor and() {
        Junctor result = new Junctor(" and ");
        result.setBegin("(");
        result.setEnd(")");
        return result;
    }

}
