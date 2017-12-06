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


        String[] listing = SambaFileClient.listPathContent(
                properties.getProperty("export.test.smb.url"),
                properties.getProperty("export.test.smb.user.domain"),
                properties.getProperty("export.test.smb.user"),
                properties.getProperty("export.test.smb.password")
        );

        for (String list : listing) {
            System.out.println(list);
        }


        SambaFileClient client = new SambaFileClient(
                properties.getProperty("export.test.smb.url"),
                properties.getProperty("export.test.smb.user.domain"),
                properties.getProperty("export.test.smb.user"),
                properties.getProperty("export.test.smb.password")
        );
        Path sambaCloneDir =  Paths.get(new File("").getAbsolutePath(), "clone-of-samba-drive");
        if (!Files.exists(sambaCloneDir)) {
            Files.createDirectories(sambaCloneDir);
        }
        client.download(sambaCloneDir.toString());
        System.out.println();

    }

}