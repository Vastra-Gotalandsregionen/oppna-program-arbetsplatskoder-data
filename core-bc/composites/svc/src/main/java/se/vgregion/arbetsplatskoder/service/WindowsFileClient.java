package se.vgregion.arbetsplatskoder.service;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

public class WindowsFileClient {

    public static void putFile(String here, String withContent, String domain, String user, String pass) {
        try {
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, user, pass);
            SmbFile smbFile = new SmbFile(here, auth);
            SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile);
            smbfos.write(withContent.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
