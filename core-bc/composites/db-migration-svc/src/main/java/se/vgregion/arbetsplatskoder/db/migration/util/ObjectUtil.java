package se.vgregion.arbetsplatskoder.db.migration.util;

public class ObjectUtil {

    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        return o1.equals(o2);
    }

    public static String join(String junctor, Iterable i) {
        StringBuilder sb = new StringBuilder();
        for (Object o : i) {
            sb.append(o);
            sb.append(junctor);
        }
        if (sb.length() > 0)
            sb.delete(sb.length() - junctor.length(), sb.length());
        return sb.toString();
    }

    public static boolean isEmptyOrNull(String s) {
        if (s == null) return true;
        return "".equals(s.trim());
    }

    public static <T> T def(T... ofThese) {
        for (T t : ofThese) {
            if (t != null)
                return t;
        }
        return null;
    }

    public static int hashCode(Object forThat) {
        if (forThat == null)
            return 0;
        return forThat.hashCode();
    }

}