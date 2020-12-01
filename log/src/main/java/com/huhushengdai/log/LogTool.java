package com.huhushengdai.log;

/**
 * Date： 2020/12/1
 * Description:
 *
 * @version 1.0
 */
public class LogTool {

    //默认使用系统Log的等级制度
    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * 是否开启日志
     */
    private static boolean LOG_ENABLE = true;

    private static LogHandler LOG_HANDLER = new DefaultLogHandler();

    public static void setLogHandler(LogHandler logHandler) {
        LOG_HANDLER = logHandler;
    }

    public static void setLogEnable(boolean enable) {
        LOG_ENABLE = enable;
    }

    public static void d(String msg) {
        if (!LOG_ENABLE || LOG_HANDLER == null) {
            return;
        }
        print(msg, DEBUG);
    }

    public static void i(String msg) {
        if (!LOG_ENABLE || LOG_HANDLER == null) {
            return;
        }
        print(msg, INFO);
    }

    public static void w(String msg) {
        if (!LOG_ENABLE || LOG_HANDLER == null) {
            return;
        }
        print(msg, WARN);
    }

    public static void e(String msg) {
        if (!LOG_ENABLE || LOG_HANDLER == null) {
            return;
        }
        print(msg, ERROR);
    }

    private static void print(String msg, int level) {
        final LogHandler logHandler = LOG_HANDLER;
        if (logHandler == null) {
            return;
        }
        Thread currentThread = Thread.currentThread();
        //默认打印第5层栈信息
        StackTraceElement stackTrace = currentThread.getStackTrace()[4];
        logHandler.onLog(currentThread.getName(), stackTrace.getFileName(), stackTrace.getLineNumber(), msg, level);
    }
}
