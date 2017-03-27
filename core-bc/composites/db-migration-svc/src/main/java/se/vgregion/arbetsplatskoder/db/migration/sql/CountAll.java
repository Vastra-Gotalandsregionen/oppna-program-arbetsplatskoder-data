package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.util.List;

/**
 * Created by clalu4 on 2017-01-07.
 */
public class CountAll implements ProducesSql {
    @Override
    public void toSql(StringBuilder sb, List values) {
        sb.append(" count(*) ");
    }
}
