package se.vgregion.arbetsplatskoder.db.migration.util;

import java.io.*;
import java.nio.file.Path;

/**
 * Created by clalu4 on 2016-11-17.
 */
public class Zerial {

    public static void toFile(Object obj, Path toHere) {
        try {
            toFileImpl(obj, toHere);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void toFileImpl(Object obj, Path toHere) throws IOException {
        FileOutputStream fout = new FileOutputStream(toHere.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(obj);
        oos.close();
    }

    public static <T> T fromFile(Class<T> havingType, Path whichIsStoredHere) {
        try {
            return fromFileImpl(havingType, whichIsStoredHere);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromFileImpl(Class<T> havingType, Path whichIsStoredHere) throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(whichIsStoredHere.toFile());
        ObjectInputStream ois = new ObjectInputStream(fin);
        return  (T) ois.readObject();
    }

    public static String toText(InputStream input) {
        try(BufferedInputStream bis = new BufferedInputStream(input);ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
            try {
                int result = 0;
                while (result != -1) {
                    buf.write((byte) result);
                    result = bis.read();
                }
                return buf.toString("UTF-8");
            } finally {
                input.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
