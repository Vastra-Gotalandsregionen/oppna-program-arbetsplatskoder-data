package se.vgregion.arbetsplatskoder.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class SambaFileClientTest {


    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        Path pp = Paths.get(System.getProperty("user.home"), ".app", "arbetsplatskoder", "samba.test.properties");
        properties.load(Files.newInputStream(pp));

        System.out.println(
            Arrays.asList(
                SambaFileClient.listPathContent(
                    properties.getProperty("export.test.smb.url"),
                    properties.getProperty("export.test.smb.user.domain"),
                    properties.getProperty("export.test.smb.user"),
                    properties.getProperty("export.test.smb.password")
                )
            )
        );
    }

}