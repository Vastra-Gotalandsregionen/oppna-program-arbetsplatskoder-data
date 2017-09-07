package se.vgregion.arbetsplatskoder.db.migration.level;

import org.junit.Test;
import se.vgregion.arbetsplatskoder.db.migration.AbstractJob;
import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Table;

import java.util.*;

public class LevelsTest {

    public static void main(String[] args) {
        List<Map<String, Object>> fileItems = ImportFile.getImportFile().getItems();
        Set<String> sumNiv1names = new TreeSet<>();
        Set<String> sumNiv2names = new TreeSet<>();
        Set<String> sumNiv3names = new TreeSet<>();
        for (Map<String, Object> fileItem : fileItems) {
            sumNiv1names.add((String) fileItem.get("Summeringsnivå 1"));
            sumNiv2names.add((String) fileItem.get("Summeringsnivå 2"));
            sumNiv3names.add((String) fileItem.get("Summeringsnivå 3"));
        }

        ConnectionExt con = AbstractJob.getMainConnectionExt();
        Table prodn1type = con.getSchemas("public").get(0).getTable("prodn1");

        List<Map<String, Object>> result = con.query("select distinct kortnamn from prodn1", 0, 10000);
        Set<String> itemIds = new TreeSet<>();
        for (Map<String, Object> prodn1 : result) {
            //itemIds.add((String) prodn1.get("kortnamn"));
            if (!sumNiv1names.contains(prodn1.get("kortnamn"))) {
                Map<String, Object> what = new HashMap<>();
                Map<String, Object> where = new HashMap<>();
                what.put("raderad", true);
                where.put("kortnamn", prodn1.get("kortnamn"));
                int count = con.update(prodn1type, what, where);
                System.out.println("Raderade " + prodn1.get("kortnamn") + " " + count);
                System.out.println("Antal data med koden " + con.query("select * from data where prodn1 in (select id from prodn1 where kortnamn = ?)", 0, 10000, prodn1.get("kortnamn")).size());
            }
        }
    }

    @Test
    public void disableProdnsNotInImport() {
        Levels levels = new Levels(AbstractJob.getMainConnectionExt());
        levels.disableProdnsNotInImport();
    }

}