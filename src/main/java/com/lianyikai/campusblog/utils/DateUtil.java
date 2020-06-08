package com.lianyikai.campusblog.utils;

import java.util.Date;

public class DateUtil {
    /*
    * 计算间隔时间/时
    * */
    public static double overTime(Date start, Date end) {
        long duration = end.getTime() - start.getTime();
        return duration * 1.0 / (1000 * 60 * 60);
    }
}
