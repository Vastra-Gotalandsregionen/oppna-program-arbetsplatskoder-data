package se.vgregion.arbetsplatskoder.db.migration.sql;

import java.util.List;

/**
 * Created by clalu4 on 2017-01-20.
 */
public class Atom<T> implements ProducesSql {

    private T value;

    public Atom() {
        super();
    }

    public Atom(T value) {
        super();
        this.value = value;
    }

    @Override
    public void toSql(StringBuilder sb, List values) {
        sb.append(value);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
