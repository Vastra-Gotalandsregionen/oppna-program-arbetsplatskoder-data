package se.vgregion.arbetsplatskoder.intsvc.controller;

import java.util.Map;

/**
 * Created by clalu4 on 2017-03-27.
 */
public class ControllerUtil {

    public static String formatBeforeJpaUsage(String o) {
        if (o.startsWith("\"") && o.endsWith("\"")) {
            o = o.substring(1, o.length() - 2);
        }
        return o;
    }

    public static void formatBeforeJpaUsage(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.setValue(formatBeforeJpaUsage(entry.getValue().toString()));
        }
        map.remove("callback");
    }

}
