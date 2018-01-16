package se.vgregion.arbetsplatskoder.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static Date aYearAgo() {
        LocalDateTime ldt = LocalDateTime.ofInstant(new Date().toInstant(), TimeZone.getDefault().toZoneId());
        LocalDateTime now = LocalDateTime.of(ldt.getYear() - 1, ldt.getMonth(), ldt.getDayOfMonth(), 0, 0, 0, 0);
        ZonedDateTime zdt = now.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
}
