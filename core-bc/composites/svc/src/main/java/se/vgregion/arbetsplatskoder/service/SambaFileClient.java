package se.vgregion.arbetsplatskoder.service;

import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SambaFileClient {

    private SambaFileClient() {
    }

    public static void putFile(String here, byte[] withContent, String domain, String user, String pass) {
        try {
            SmbFile smbFile = new SmbFile(here, getCifsContext(domain, user, pass));
            if (!smbFile.exists()) {
                smbFile.createNewFile();
            }
            try (SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile)) {
                smbfos.write(withContent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createPath(String path, String domain, String user, String pass) {
        try {
            SmbFile smbFile = new SmbFile(path, getCifsContext(domain, user, pass));
            if (!smbFile.exists()) {
                smbFile.mkdirs();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String[] listPathContent(String path, String domain, String user, String pass) {
        try {
            if (!path.endsWith("/")) {
                path += "/";
            }
            SmbFile smbFile = new SmbFile(path, getCifsContext(domain, user, pass));

            List<String> names = new ArrayList<>();
            if (smbFile.exists()) {
                for (SmbFile file : smbFile.listFiles()) {
                    names.add(file.getName());
                }
            }
            return names.toArray(new String[names.size()]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SmbFile[] listSambaFiles(String path, CIFSContext cifsContext) {
        try {
            if (!path.endsWith("/")) {
                path += "/";
            }

            SmbFile smbFile = new SmbFile(path, cifsContext);

            List<SmbFile> names = new ArrayList<>();
            if (smbFile.exists()) {
                return smbFile.listFiles();
            }
            return names.toArray(new SmbFile[names.size()]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void download(String path, String domain, String user, String pass, String toThatLocalPath) {
        download(path, getCifsContext(domain, user, pass), toThatLocalPath);
    }

    private static String removeTrailing(String str, char c) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == c) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static boolean download(String path, CIFSContext cifsContext, String destinationPath) {
        System.setProperty("jcifs.encoding", "ISO-8859-1");
        SmbFile[] files = listSambaFiles(path, cifsContext);

        if (files == null)
            return false;

        String destFileName;
        PrintWriter fileOutputStream;

        for (SmbFile smbFile : files) {
            try {
                destFileName = destinationPath + (destinationPath.endsWith(File.separator) ? "" : File.separator) + smbFile.getName();
                destFileName = removeTrailing(destFileName, '/');
                if (smbFile.isDirectory()) {
                    Files.createDirectories(Paths.get(destFileName));
                    String deeperRemotePath = path + (path.endsWith("/") ? "" : "/") + smbFile.getName();
                    download(deeperRemotePath, cifsContext, destFileName);
                } else {
                    fileOutputStream = new PrintWriter(destFileName, "ISO-8859-1"); // new FileOutputStream(destFileName);
                    fileOutputStream.write(toString(smbFile.getInputStream()));
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private static CIFSContext getCifsContext(String domain, String user, String pass) {
        CIFSContext cifsContext = SingletonContext.getInstance();

        NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(
                cifsContext,
                domain,
                user,
                pass
        );

        return cifsContext.withCredentials(authentication);
    }

    static String toString(java.io.InputStream is) throws IOException {
        String result = slurp(is, 128);
        is.close();
        return result;
    }

    private static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "ISO-8859-1")) {
            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return out.toString();
    }

}
