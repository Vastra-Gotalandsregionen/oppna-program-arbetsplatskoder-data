package se.vgregion.arbetsplatskoder.util;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ExcelUtilIT {

    @Test
    public void readRows() {

        List<Map<String, String>> maps = ExcelUtil.readRows("/opt/Arbetsplatskoder_justering_ny_org_2022-12-13.xlsx");

        System.out.println(maps);
    }
}
