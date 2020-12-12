package com.yb.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// 格式化输出日期时间
public class AppDateUtils {

    public static String getFormatTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static String getFormatTime(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    public static String getFormatTime(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

}
