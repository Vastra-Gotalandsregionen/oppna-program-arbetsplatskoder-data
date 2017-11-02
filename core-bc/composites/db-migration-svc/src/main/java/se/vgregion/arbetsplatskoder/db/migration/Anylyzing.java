package se.vgregion.arbetsplatskoder.db.migration;

import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Column;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Schema;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Anylyzing {

    public static void main(String[] args) {
        ConnectionExt connection = AbstractJob.getMainConnectionExt();
        List<Map<String, Object>> historik = connection.query(
            "select h.* from historik h, data d where d.id = h.data_id and d.arbetsplatskod like ?",
            0,
            100_000,
            "%1551015%"
        );

        Set set = new HashSet();
        for (Map<String, Object> hist : historik) {
            System.out.println(hist);
            set.add(hist.get("andring_datum"));
        }
        System.out.println(set);

        for (Column column : connection.getSchemas("public").get(0).getTable("historik").getColumns()) {
            System.out.println(column.getColumnName());
        }

    }

}
