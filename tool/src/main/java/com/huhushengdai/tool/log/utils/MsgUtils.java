package com.huhushengdai.tool.log.utils;

import java.util.Locale;

/**
 * Date： 2020/12/7
 * Description:
 *
 * @version 1.0
 */
public class MsgUtils {

    public static String getMsg(String threadName, String fileName, String methodName, int lineNum, String msg) {
        return String.format(Locale.CHINA, "[%s] (%s:%d) [%s] %s", threadName, fileName, lineNum, methodName, msg);
    }
}
