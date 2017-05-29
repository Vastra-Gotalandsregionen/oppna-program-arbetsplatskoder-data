package se.vgregion.arbetsplatskoder.db.migration;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by clalu4 on 2017-03-23.
 */
public class JobTest {

    @Test
    public void main() throws IOException {
        Properties properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(System.getProperty("user.home"), ".app", "arbetsplatskoder", "legacy.jdbc.properties"));) {
            properties.load(reader);
        }
        System.out.println(properties);
    }

    private Properties getLegacyProperties() {
        Properties properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(System.getProperty("user.home"), ".app", "arbetsplatskoder", "legacy.jdbc.properties"));) {
            properties.load(reader);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return properties;
    }



}