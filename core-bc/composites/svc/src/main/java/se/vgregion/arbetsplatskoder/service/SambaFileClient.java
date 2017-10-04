package se.vgregion.arbetsplatskoder.service;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

public class SambaFileClient {

    public static void putFile(String here, String withContent, String domain, String user, String pass) {
        try {
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, user, pass);
            SmbFile smbFile = new SmbFile(here, auth);
            if (!smbFile.exists()) {
                smbFile.createNewFile();
            }
            try (SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile)) {
                smbfos.write(withContent.getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createPath(String path, String domain, String user, String pass) {
        try {
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, user, pass);
            SmbFile smbFile = new SmbFile(path, auth);
            if (!smbFile.exists()) {
                smbFile.mkdirs();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] listPathContent(String path, String domain, String user, String pass) {
        try {
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, user, pass);
            if (!path.endsWith("/")) {
                path += "/";
            }
            SmbFile smbFile = new SmbFile(path, auth);
            if (smbFile.exists()) {
                return smbFile.list();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
