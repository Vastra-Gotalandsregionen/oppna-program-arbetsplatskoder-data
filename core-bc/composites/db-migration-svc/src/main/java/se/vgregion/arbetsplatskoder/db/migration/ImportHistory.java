package se.vgregion.arbetsplatskoder.db.migration;

import com.fasterxml.jackson.databind.ObjectMapper;
import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportHistory {

    public static void main(String[] args) throws ParseException, IOException {
        ConnectionExt connection = AbstractJob.getMainConnectionExt();

        connection.execute("delete from archived_data where id < 0");

        List<Map<String, Object>> orphans = connection.query(
            "select distinct d.* from data d where not d.id in (select data_id from historik)",
            0,
            100_000
        );

        writeJsonToDisc(orphans,
            Paths.get(
                System.getProperty("user.home"), ".app", "arbetsplatskoder", "historik.orphans.json"
            ).toString()
        );

        List<Map<String, Object>> data = connection.query(
            "select distinct d.* from data d where d.id in (select data_id from historik)",
            0,
            100_000
        );

        List<Map<String, Object>> historik = connection.query(
            "select * from historik order by andring_datum desc",
            0, 100_000);

        Map<Integer, Map<String, Object>> dataMap = new TreeMap<>();
        for (Map<String, Object> item : data) {
            dataMap.put((Integer) item.get("id"), item);
        }

        MappedList<Integer, Map<String, Object>> historikMap = new MappedList<>();

        for (Map<String, Object> item : historik) {
            historikMap.get(item.get("data_id")).add(item);
        }

        System.out.println("Copying items from historik to archived_data. ");
        System.out.print("Processed items: ");
        int i = 1;
        for (Map<String, Object> item : data) {
            Integer id = new Integer((Integer) item.get("id"));
            List<Map<String, Object>> changes = historikMap.get(id);
            Map<String, Object> accumulatedChange = new TreeMap<>(item);
            for (Map<String, Object> change : changes) {
                putAllNonBlankValues(change, accumulatedChange);
                TreeMap<String, Object> archivedData = new TreeMap<>(accumulatedChange);
                Integer changeId = (Integer) change.get("id");
                changeId = changeId * -1;
                archivedData.put("id", changeId);
                convertFieldFromStringToTimestamp(archivedData, "till_datum");
                convertFieldFromStringToTimestamp(archivedData, "from_datum");
                convertStringToBoolean(archivedData, "apodos");
                convertLeveransToInteger(archivedData, "leverans");
                convertFromTextToInteger(archivedData, "fakturering");
                archivedData.put("andringsdatum", archivedData.get("andring_datum"));
                if (archivedData.get("user_id") instanceof String) {
                    Integer userId = Integer.parseInt((String) archivedData.get("user_id"));
                    archivedData.put("user_id", userId);
                }
                archivedData.put("replacer", id);
                // System.out.println(archivedData);
                connection.insert("archived_data", archivedData);
                if (i % 100 == 0) {
                    System.out.print(" " + i);
                    if (i % 2000 == 0) {
                        System.out.println();
                    }
                }
                i++;
            }
        }
        connection.commit();
    }

    static void writeJsonToDisc(Object object, String toThatFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(toThatFile), object);
    }

    static Map<String, Integer> leveransDomain = new HashMap<>();

    static {
        leveransDomain.put("1", 1);
        leveransDomain.put("2", 2);
        leveransDomain.put("0", 0);
        leveransDomain.put("", -1);
        leveransDomain.put("transport", -2);
        leveransDomain.put("tr", -3);
    }

    public static void convertFromTextToInteger(Map<String, Object> inThat, String key) {
        Object value = inThat.get(key);
        if (value instanceof String) {
            String text = (String) value;
            text = text.trim();
            if (text.equals("")) {
                return;
            }
            inThat.put(key, Integer.parseInt(text));
        }
    }

    public static void convertLeveransToInteger(Map<String, Object> inThat, String key) {
        Object value = inThat.get(key);

        /*select leverans, count(*) from historik group by leverans
            "";34
            "transport";15
            "1";72
            "2";59
            "0";2852
            "tr";7
            "";11946
            "transp";1
        */
        if (value instanceof String) {
            String text = (String) value;
            text = text.trim();
            inThat.put(key, leveransDomain.get(text));
        }
    }

    public static void convertStringToBoolean(Map<String, Object> inThat, String key) {
        Object value = inThat.get(key);
        if (value instanceof String) {
            String text = (String) value;
            text = text.trim();
            if (text.equals("")) {
                inThat.put(key, false);
            } else {
                if (text.equals("true") || text.equals("1")) {
                    inThat.put(key, true);
                } else {
                    inThat.put(key, false);
                }
            }
        } else if (value == null) {
            inThat.put(key, false);
        }
    }

    public static void convertFieldFromStringToTimestamp(Map<String, Object> inThat, String key) throws ParseException {
        if (inThat.get(key) instanceof String) {
            String that = (String) inThat.get(key);
            Timestamp ts = new Timestamp(sdf.parse(that).getTime());
            inThat.put(key, ts);
        }
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    static void putAllNonBlankValues(Map<String, Object> fromThat, Map<String, Object> intoThis) {
        for (String key : fromThat.keySet()) {
            if (intoThis.containsKey(key)) {
                Object ftv = fromThat.get(key);
                if (ftv != null && !ftv.toString().trim().equals("")) {
                    intoThis.put(key, ftv);
                }
            }
        }
    }

    private static class MappedList<K, V> extends TreeMap<K, List<V>> {

        @Override
        public List<V> get(Object key) {
            if (!containsKey(key)) {
                put((K) key, new ArrayList<>());
            }
            return super.get(key);
        }

    }

}
