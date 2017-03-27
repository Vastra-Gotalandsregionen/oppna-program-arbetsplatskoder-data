package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.util.List;

/**
 * Created by clalu4 on 2017-01-02.
 */
public class Match implements Condition {

    private final ProducesSql first, second;

    private String operator;

    public Match(ProducesSql first, String operator, ProducesSql second) {
        super();
        this.first = first;
        this.operator = operator;
        this.second = second;
    }

    @Override
    public void toSql(StringBuilder sb, List values) {
        first.toSql(sb, values);
        sb.append(operator);
        second.toSql(sb, values);
    }
}
