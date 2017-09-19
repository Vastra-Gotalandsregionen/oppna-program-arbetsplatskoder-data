package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Schema;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Table;

import java.util.*;

public abstract class AbstractLevels {

    private final ConnectionExt connection;

    ImportFile importFile = ImportFile.getImportFile();

    // private static Map<String, Map<String, Set<String>>> fromFile;

    Prodn1Dao prodn1Dao; // = new Trunk(connection, "prodn1");
    Prodn2Dao prodn2Dao; // = new Branch(connection, "prodn2");
    Prodn3Dao prodn3Dao; // = new Leaf(connection, "prodn3");

    public AbstractLevels(ConnectionExt connection) {
        this.connection = connection;
        prodn1Dao = new Prodn1Dao(connection);
        prodn2Dao = new Prodn2Dao(connection);
        prodn3Dao = new Prodn3Dao(connection);
    }

    public void markOldLevelsAsObsolete() {
        connection.update("update prodn1 set raderad = true where id > 0");
        connection.update("update prodn2 set raderad = true where id > 0");
        connection.update("update prodn3 set raderad = true where id > 0");
        connection.commit();
    }


    public void pairCodeWithNewLevels() {
        Table table = connection.getSchemas("public").get(0).getTable("data");

        int totalChangeCount = 0;

        for (Map<String, Object> map : importFile.getItems()) {

            // List<Map<String, Object>> data = connection.query("select * from data where arbetsplatskod = ?", 0, 100, map.get("Arbetsplatskod"));
            Map<String, Object> trunk = (Map<String, Object>) map.get("trunk");
            Map<String, Object> branch = (Map<String, Object>) map.get("branch");
            Map<String, Object> leaf = (Map<String, Object>) map.get("leaf");

            assert trunk != null;
            assert branch != null;
            assert leaf != null;

            Map<String, Object> withThatNewData = new HashMap<>();
            withThatNewData.put("prodn1", trunk.get("id"));
            withThatNewData.put("prodn3", leaf.get("id"));

            Map<String, Object> matchingThat = new HashMap<>();
            matchingThat.put("arbetsplatskodlan", map.get("Arbetsplatskod"));

            int changeCount = connection.update(table, withThatNewData, matchingThat);
            totalChangeCount += changeCount;
            System.out.println(map.get("Arbetsplatskod") + " ändrad på " + changeCount + " rader.");
        }
        connection.commit();
        System.out.println("Totalt antal ändrade rader " + totalChangeCount);
    }

    public void compareDatabaseWithFileContent() {
        int found = 0;
        List<Map<String, Object>> items = importFile.getItems(), notFound = new ArrayList<>();
        for (Map<String, Object> item : items) {
            List<Map<String, Object>> matches = connection.query(
                "select * from data where arbetsplatskodlan = ?",
                0,
                100,
                item.get("Arbetsplatskod")
            );
            if (matches.isEmpty()) {
                // System.out.println("Finns inte i databasen " + item);
                notFound.add(item);
            } else {
                found++;
            }
        }
        System.out.println("Hittade " + found + " totalt antal " + items.size() + " diffen är " + (items.size() - found));
        TreeSet<String> missingCodes = new TreeSet<>();
        for (Map<String, Object> nf : notFound) {
            System.out.println(nf);
            missingCodes.add((String) nf.get("Arbetsplatskod"));
        }

        System.out.println("'" + String.join("', '", missingCodes) + "'");
    }

    public void undoPreviousLevelCreation() {
        connection.update("update data set prodn1=null where prodn1 < 0");
        connection.update("update data set prodn3=null where prodn3 < 0");
        int count = connection.update("delete from prodn3 where id < 0");
        System.out.println("Removed " + count + " from prodn3 ");
        count = connection.update("delete from prodn2 where id < 0");
        System.out.println("Removed " + count + " from prodn2 ");
        count = connection.update("delete from prodn1 where id < 0");
        System.out.println("Removed " + count + " from prodn1 ");
        connection.commit();
    }


/*
    public void copyLevelsFromFileIntoDatabase() {
        Map<String, Map<String, Set<Map<String, Object>>>> fromFile = importFile.getTree();
        for (String rootName : fromFile.keySet()) {
            Map<String, Object> trunk = prodn1Dao.create(null, rootName);
            Map<String, Set<Map<String, Object>>> middle = fromFile.get(rootName);
            for (String branchName : middle.keySet()) {
                Map<String, Object> branch = prodn2Dao.create(trunk, branchName);
                Set<String> leafNames = new HashSet<>();
                for (Map<String, Object> leafName : middle.get(branchName)) {
                    leafNames.add((String) leafName.get("Summeringsnivå 3"));
                    // leafs.create(b, leafName);
                }
                for (String l : leafNames) {
                    Map<String, Object> leaf = prodn3Dao.create(branch, l);
                    for (Map<String, Object> leafName : middle.get(branchName)) {
                        leafName.put("trunk", trunk);
                        leafName.put("branch", branch);
                        leafName.put("leaf", leaf);
                    }
                }
            }
        }
        connection.commit();
        System.out.println("Inserted " + prodn1Dao.insertCount + " items into prodn1");
        System.out.println("Inserted " + prodn2Dao.insertCount + " items into prodn2");
        System.out.println("Inserted " + prodn3Dao.insertCount + " items into prodn3");
    }
*/


    public void runImportWork() {
        Schema pub = connection.getSchemas("public").get(0);
        final Table table = pub.getTable("data");
        for (Map<String, Object> map : importFile.getItems()) {
            List<Map<String, Object>> perfectMatch = connection.query("select * from prodn1 p1 " +
                    "join prodn2 p2 on p2.prodn1 = p1.id " +
                    "join prodn3 p3 on p3.prodn2 = p2.id " +
                    "join data d on d.prodn1 = p1.id and d.prodn3 = p3.id " +
                    "where p1.kortnamn = ? and p2.kortnamn = ? and p3.kortnamn = ? " +
                    "and p1.raderad = false and p2.raderad = false and p3.raderad = false and d.arbetsplatskodlan = ? ",
                0, 10,
                map.get("Summeringsnivå 1"),
                map.get("Summeringsnivå 2"),
                map.get("Summeringsnivå 3"),
                map.get("Arbetsplatskod"));
            if (!perfectMatch.isEmpty()) {
                continue;
            }

            Map<String, Object> what = new HashMap<>();
            Map<String, Object> where = new HashMap<>();
            List<Map<String, Object>> ps = findOrCreateProdnLevelsInDatabase(
                (String) map.get("Summeringsnivå 1"),
                (String) map.get("Summeringsnivå 2"),
                (String) map.get("Summeringsnivå 3")
            );
            what.put("prodn1", ps.get(0).get("id"));
            what.put("prodn3", ps.get(2).get("id"));
            where.put("arbetsplatskodlan", map.get("Arbetsplatskod"));
            connection.update(table, what, where);
        }
        connection.commit();
    }

    public List<Map<String, Object>> findOrCreateProdnLevelsInDatabase(String kortNamnProdn1, String kortNamnProdn2, String kortNamnProdn3) {
        List<Map<String, Object>> p1s = prodn1Dao.find(kortNamnProdn1);
        Map<String, Object> p1 = null;
        if (p1s.size() == 0) {
            p1 = prodn1Dao.create(null, kortNamnProdn1);
        } else {
            p1 = p1s.get(0);
        }

        List<Map<String, Object>> p2s = connection.query("select p2.* from prodn2 p2 where p2.prodn1 = ? and p2.kortnamn = ?", 0, 100, p1.get("id"), kortNamnProdn2);
        Map<String, Object> p2 = null;
        if (p2s.size() == 1) {
            p2 = p2s.get(0);
        } else {
            p2 = prodn2Dao.create(p1, kortNamnProdn2);
        }

        List<Map<String, Object>> p3s = connection.query("select p3.* from prodn3 p3 where p3.prodn2 = ? and p3.kortnamn = ?", 0, 100, p2.get("id"), kortNamnProdn3);
        Map<String, Object> p3 = null;
        if (p3s.size() == 1) {
            p3 = p3s.get(0);
        } else {
            p3 = prodn3Dao.create(p2, kortNamnProdn3);
        }

        return Arrays.asList(p1, p2, p3);
    }

    public void disableProdnsNotInImport() {
        disableProdn(1, getConcatenatedKeyFromFileColumns("Summeringsnivå 1"));
        disableProdn(2, getConcatenatedKeyFromFileColumns("Summeringsnivå 1", "Summeringsnivå 2"));
        disableProdn(3, getConcatenatedKeyFromFileColumns("Summeringsnivå 1", "Summeringsnivå 2", "Summeringsnivå 3"));
    }

    Set<String> getConcatenatedKeyFromFileColumns(String... withThatName) {
        Set<String> result = new TreeSet<>();
        for (Map<String, Object> item : importFile.getItems()) {
            StringBuilder sb = new StringBuilder();
            for (String columnName : withThatName) {
                sb.append((String) item.get(columnName));
            }
            result.add(sb.toString());
        }
        return result;
    }

    void disableProdn(int no, Set<String> sumNivNamesFromFile) {
        assert no == 1 || no == 2 || no == 3;
        String tableName = "prodn" + no;

        List<Map<String, Object>> result = null;
        switch (no) {
            case 1:
                result = connection.query("select distinct id, kortnamn, kortnamn as concatkey from prodn1", 0, 10000);
                break;
            case 2:
                result = connection.query("select distinct p2.id as id, p2.kortnamn as kortnamn, (p1.kortnamn || p2.kortnamn) as concatkey from prodn2 p2 left join prodn1 p1 on p2.prodn1=p1.id", 0, 10000);
                break;
            case 3:
                result = connection.query("select distinct p3.id as id, p3.kortnamn as kortnamn, (p1.kortnamn || p2.kortnamn || p3.kortnamn) as concatkey from prodn3 p3 left join prodn2 p2 on p3.prodn2=p2.id left join prodn1 p1 on p2.prodn1=p1.id", 0, 10000);
                break;
        }

        final Table prodnType = connection.getSchemas("public").get(0).getTable(tableName);

        for (Map<String, Object> prodn : result) {
            if (!sumNivNamesFromFile.contains(prodn.get("concatkey"))) {
                Map<String, Object> what = new HashMap<>();
                Map<String, Object> where = new HashMap<>();
                what.put("raderad", true);
                where.put("id", prodn.get("id"));
                int count = connection.update(prodnType, what, where);
                System.out.println("Raderade " + prodn.get("kortnamn") + " " + count);
            }
        }
    }


    public void commit(){
        connection.commit();
    }
}
