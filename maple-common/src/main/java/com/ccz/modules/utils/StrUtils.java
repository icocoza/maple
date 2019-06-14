package com.ccz.modules.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class StrUtils {

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
}
