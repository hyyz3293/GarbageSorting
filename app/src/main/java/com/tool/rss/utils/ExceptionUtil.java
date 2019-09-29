package com.tool.rss.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 * @author jack
 * @date 2017.08.20
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ExceptionUtil {
    /**
     * 获取堆栈信息
     *
     * @param exception 异常
     * @return 堆栈信息
     */
    public static String getStackTrace(Exception exception) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer, true));
        return writer.toString();
    }
}
