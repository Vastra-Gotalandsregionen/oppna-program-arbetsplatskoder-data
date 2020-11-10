package se.vgregion.arbetsplatskoder.domain;

import java.util.HashMap;
import java.util.Map;

public enum AgarformEnum {
    LANDSTING("1", "Landsting"),
    KOMMUN("2", "Kommun"),
    STATLIG("3", "Statlig"),
    PRIVAT_VARDAVTAL("4", "Privat, vårdavtal"),
    PRIVAT_LAKARERSATTNING("5", "Privat, enl lag om läkarvårdsersättning"),
    PRIVAT_UTAN_OFFENTLIG_FINANSIERING("6", "Privat, utan offentlig finansiering"),
    OVRIGT("9", "Övrigt");
    
    private static final Map<String, AgarformEnum> keyLabelMap = new HashMap<>();

    private final String key;
    private final String label;

    AgarformEnum(String key, String label) {
        this.label = label;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public static AgarformEnum getByKey(String key) {
        if (keyLabelMap.size() == 0) {
            for (AgarformEnum agarformEnum : AgarformEnum.values()) {
                keyLabelMap.put(agarformEnum.key, agarformEnum);
            }
        }

        return keyLabelMap.get(key);
    }
}
