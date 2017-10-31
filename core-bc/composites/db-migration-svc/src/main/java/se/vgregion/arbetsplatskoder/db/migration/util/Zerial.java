package se.vgregion.arbetsplatskoder.db.migration.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Created by clalu4 on 2016-11-17.
 */
public class Zerial {

    public static void toJsonFile(Object obj, Path toHere) {
        try {
            toJsonFileImpl(obj, toHere);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void toJsonFileImpl(Object obj, Path toHere) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(toHere.toString()), obj);
    }

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

    public static <T> T fromJsonFile(Class<T> havingType, Path whichIsStoredHere) {
        try {
            return fromJsonFileImpl(havingType, whichIsStoredHere);
        } catch (Exception e) {
            System.out.println("Failed to convert content of file: " + whichIsStoredHere);
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromJsonFileImpl(Class<T> havingType, Path whichIsStoredHere) throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(whichIsStoredHere.toString()), havingType);
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

    public static String toText(Clob clob) {
        try {
            Reader in = clob.getCharacterStream();
            int line = -1;
            StringBuilder rslt = new StringBuilder();
            while ((line = in.read()) != -1) {
                rslt.append((char)line);
            }
            return rslt.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(Blob blob) {
        try {
            int blobLength = (int) blob.length();
            byte[] bytes = blob.getBytes(1, blobLength);
            //release the blob and free up memory. (since JDBC 4.0)
            blob.free();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
