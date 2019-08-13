package com.ccz.modules.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class KeyGen {
    static int seq = 0;

    public static String makeKey() {    //ex, test:fbc671c6-1a7b-481d-8034-1d7ae3c733c8 (5+36= 41char)
        return String.format("%s-%04d", UUID.randomUUID(), ++seq % 10000);
    }

    public static String makeKey(String prefix) {    //ex, test:fbc671c6-1a7b-481d-8034-1d7ae3c733c8 (5+36= 41char)
        return String.format("%s%s", prefix, UUID.randomUUID());
    }

    public static String makeKey(String prefix, String footer) {        //ex, test:d6340c15-43f4-4551-8cf6-08a15dc3eebd:20181214095732
        return String.format("%s%s%s", prefix, UUID.randomUUID(), footer);
    }

    public static String makeKey(String prefix, int seq) {    //ex, test:2cee287e-ef2e-4692-b6ad-910b0ab73984:0009
        return String.format("%s%s%04d", prefix, UUID.randomUUID(), seq % 10000);
    }

    static public String makeKeyWithSeq(String prefix) {	//ex, test:2cee287e-ef2e-4692-b6ad-910b0ab73984:0009
        return String.format("%s%s%04d", prefix, UUID.randomUUID().toString().replaceAll("-", ""), ++seq % 10000);
    }
    static public String makeKeyWithDate(String prefix) {	//ex, test:2cee287e-ef2e-4692-b6ad-910b0ab73984:0009
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddhhmmss", Locale.KOREA);
        String nowStr = dayTime.format(new Date(time));
        return String.format("%s%s%s", prefix, UUID.randomUUID().toString().replaceAll("-", ""), nowStr);
    }

}
