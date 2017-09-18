package se.vgregion.arbetsplatskoder.service;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;
import static se.vgregion.arbetsplatskoder.service.WindowsFileClient.putFile;

public class WindowsFileClientTest {


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