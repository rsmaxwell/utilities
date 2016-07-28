package com.rsmaxwell.utilities.basic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class Timestamp {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String now() {
        Date date = new Date();
        return df.format(date);
    }

    public static String format(long time) {
        return df.format(new Date(time));
    }

    public static long parse(String string) throws ParseException {
        return df.parse(string).getTime();
    }
}
