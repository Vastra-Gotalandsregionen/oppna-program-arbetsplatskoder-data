package se.vgregion.arbetsplatskoder.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

/**
 * @author Patrik Björk
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    public static InputStream printToFile(List<String[]> result) {

        Workbook wb = new XSSFWorkbook();

        Sheet sheet = wb.createSheet("APK");

        int rownum = 0;
        for (String[] array : result) {
            Row row = sheet.createRow(rownum++);

            int cellnum = 0;

            for (String string : array) {
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(string);
            }
        }
        try {
            PipedOutputStream pos = new PipedOutputStream();

            new Thread(() -> {
                try {
                    wb.write(pos);
                    pos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }).start();

            PipedInputStream pis = new PipedInputStream(pos);

            return pis;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
