package cn.bos.monitor.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampFormatter {
    public static String getNowTimestampString(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

}
