package com.ccz.modules.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {

    private static final String URL_PATTERN = "\\(?\\b((http|https)://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
    public static List<String> extractUrls(String text) {
        ArrayList<String> links = new ArrayList<>();

        Pattern p = Pattern.compile(URL_PATTERN);
        Matcher m = p.matcher(text);
        while(m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            links.add(urlStr);
        }
        return links;
    }

    public static int findEndOfLine(final ByteBuf buffer) {
        int totalLength = buffer.readableBytes();
        int i = buffer.forEachByte(buffer.readerIndex(), totalLength, ByteProcessor.FIND_LF);
        if (i >= 0) {
            if (i > 0 && buffer.getByte(i - 1) == '\r') {
                i--;
            }
        }
        return i;
    }

    public static int findSemicolonOfLine(final ByteBuf buffer) {
        int totalLength = buffer.readableBytes();
        int i = buffer.forEachByte(buffer.readerIndex(), totalLength, ByteProcessor.FIND_SEMI_COLON);
        if (i >= 0) {
            if (i > 0 && buffer.getByte(i - 1) == '\r') {
                i--;
            }
        }
        return i;
    }

    public static LocalDateTime calendartoLocalDateTime(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    static public String getSha1(String data) {
        return DigestUtils.sha1Hex(data);
    }

    static public String getSha256(String data) {
        return DigestUtils.sha256Hex(data);
    }

    static int appendix_count=0;
    static public String getSha1Uuid(String prefix) {
        return getSha1Uuid(prefix, String.format("%04d", ++appendix_count%10000));
    }

    static public String getSha256Uuid(String prefix) {
        return getSha1Uuid(prefix, String.format("%04d", ++appendix_count%10000));
    }

    static public String getSha1Uuid(String prefix, String footer) {
        return prefix + DigestUtils.sha1Hex(UUID.randomUUID().toString() + System.currentTimeMillis()) + footer;
    }
    static public String getSha256Uuid(String prefix, String footer) {
        return prefix + DigestUtils.sha256Hex(UUID.randomUUID().toString() + System.currentTimeMillis()) + footer;
    }

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static public boolean isEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    private static final String PHONE_PATTERN = "^\\(?(\\d{3})\\)?[- ]?(\\d{3,4})[- ]?(\\d{4})$";
    static public boolean isPhone(String phone) {
        return phone.matches(PHONE_PATTERN);
    }

    static public boolean isAlphaNumeric(String name) {
        return name.matches("^[a-zA-Z0-9]+$");
    }

    static public boolean isFileName(String name) {
        return name.matches("^[A-Za-z0-9-_,\\s]+[.]{1}[A-Za-z]{3}$");
    }

    static String ALPHA_NUMERIC_PATTERN = "((?<=[a-zA-Z])(?=[0-9]))|((?<=[0-9])(?=[a-zA-Z]))";
    static public List<String> splitAlphaNumeric(String str) {
        return Arrays.asList(str.split(ALPHA_NUMERIC_PATTERN));
    }

    static String KOREAN_NUMERIC_PATTERN = "((?<=[ㄱ-ㅎ가-힣])(?=[0-9]))|((?<=[0-9])(?=[ㄱ-ㅎ가-힣]))";
    static public List<String> splitKoreanNumeric(String str) {
        return Arrays.asList(str.split(KOREAN_NUMERIC_PATTERN));
    }
    static String NUMERIC_DASH_PATTERN = "[0-9][0-9-]*[0-9]";
    static public boolean isNumericDash(String str) {
        return str.matches(NUMERIC_DASH_PATTERN);
    }

    static String NUMERIC_PATTERN = "[0-9]";
    static public boolean isNumeric(String str) {
        return str.matches(NUMERIC_DASH_PATTERN);
    }

    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
    static public boolean isImageFile(String s) {
        return s.matches(IMAGE_PATTERN);
    }

    static public String getUuid(String prefix) {
        return prefix + UUID.randomUUID().toString().replaceAll("-", "");
    }

}
