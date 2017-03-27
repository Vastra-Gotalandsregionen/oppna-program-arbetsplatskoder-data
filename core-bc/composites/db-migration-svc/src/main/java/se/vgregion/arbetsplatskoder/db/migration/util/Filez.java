package se.vgregion.arbetsplatskoder.db.migration.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by clalu4 on 2017-03-22.
 */
public class Filez {

    public static List<Finding> findDistinctWords(Path here, String afterText) {
        List<Finding> result = new ArrayList<>();
        afterText = afterText.toLowerCase();
        findDistinctWords(here, afterText, result);
        return result;
    }

    private static void findDistinctWords(Path here, String afterText, List<Finding> findings) {
        File file = here.toFile();

        if (!file.exists()) {
            throw new IllegalArgumentException(here + " must exists.");
        }

        try {
            if (file.isFile()) {
                List<String> lines = Files.readAllLines(file.toPath());
                int r = 0;
                for (String s : lines) {
                    r++;
                    List<String> justWords = Arrays.asList(s.toLowerCase().split("[^a-öÄ-Ö]"));
                    if (justWords.contains(afterText)) {
                        Finding finding = new Finding(file.toPath(), r, s);
                        findings.add(finding);
                    }
                }
            } else {
                for (File child : file.listFiles()) {
                    findDistinctWords(child.toPath(), afterText, findings);
                }
            }
        }catch (MalformedInputException mie){
            //System.out.println(here);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    public static class Finding {

        private String line;
        private Path file;
        private int rowIndex;

        public Finding(Path path, int rowIndex, String line) {
            this.file = path;
            this.rowIndex = rowIndex;
            this.line = line;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public void setRowIndex(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        public Path getFile() {
            return file;
        }

        public void setFile(Path file) {
            this.file = file;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        @Override
        public String toString() {
            return file + ";" + rowIndex + ";" + line;
        }
    }

}

