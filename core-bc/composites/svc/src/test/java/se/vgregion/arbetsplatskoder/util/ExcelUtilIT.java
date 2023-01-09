package se.vgregion.arbetsplatskoder.util;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ExcelUtilIT {

    @Test
    public void readRows() {

        List<Map<String, String>> maps = ExcelUtil.readRows();

        System.out.println(maps);
    }
}
