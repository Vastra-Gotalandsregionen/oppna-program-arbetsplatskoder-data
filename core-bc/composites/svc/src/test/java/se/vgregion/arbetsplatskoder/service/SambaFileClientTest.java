package se.vgregion.arbetsplatskoder.service;

import java.io.IOException;
import java.util.Date;

import static se.vgregion.arbetsplatskoder.service.SambaFileClient.putFile;

public class SambaFileClientTest {


    public static void main(String[] args) throws IOException {
        Date now = new Date();
        putFile(
            "smb://127.0.0.1/share/some-file.txt",
            "From Java with love. " + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds(),
            "VGREGION",
            "user",
            "secret"
        );
    }

}