package se.vgregion.arbetsplatskoder.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.*;

/**
 * @author Patrik Björk
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    public static InputStream exportToStream(List<String[]> result) {

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

            return new PipedInputStream(pos);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    // TODO temporary feature
    public static List<Map<String, String>> readRows() {

        List<Map<String, String>> result = new ArrayList<>();

        try {
            Workbook wb = new XSSFWorkbook("/opt/Arbetsplatskoder_justering_ny_org_2022-12-13.xlsx");

            Sheet sheetAt = wb.getSheetAt(0);

            Row headingRow = sheetAt.getRow(0);

            Iterator<Cell> cellIterator = headingRow.cellIterator();

            List<String> headings = new ArrayList<>();

            while (cellIterator.hasNext()) {
                Cell next = cellIterator.next();
                headings.add(next.getStringCellValue());
            }

            int lastRowIndex = sheetAt.getLastRowNum();

            int i = 1;

            while (i <= lastRowIndex) {
                Row currentRow = sheetAt.getRow(i);
                i++;

//                List<KeyValue<String, String>> keyValues = new ArrayList<>();

                Map<String, String> row = new HashMap<>();

                for (int j = 0; j < currentRow.getLastCellNum(); j++) {
                    Cell cell = currentRow.getCell(j);

                    if (cell == null) {
                        continue;
                    }

                    CellType cellType = cell.getCellType();

                    switch (cellType) {
                        case STRING:
                            row.put(headings.get(j), cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            row.put(headings.get(j), String.valueOf(Math.round(cell.getNumericCellValue())));
                            break;
                        default:
                            throw new IllegalArgumentException("CellType: " + cellType.name());
                    }
                }

                result.add(row);
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
