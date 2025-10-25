package com.rempms.rempms_notification_service.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author maleeshasa
 * @Date 2024/11/16
 */
public class ExceptionUtil {

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
