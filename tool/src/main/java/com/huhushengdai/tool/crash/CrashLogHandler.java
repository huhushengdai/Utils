package com.huhushengdai.tool.crash;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date： 2020/12/3
 * Description:
 * 收到crash信息时，存入指定路径下的CrashLog文件夹下
 * <p>
 * 保存之后，再根据是主线程，还是子线程，分别进行后续处理
 *
 * @version 1.0
 */
public class CrashLogHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = CrashLogHandler.class.getSimpleName();
    private static final String DIR_NAME = "CrashLog";

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private String mSavePath;
    private AfterCrashLog mAfterCrashLog;

    public CrashLogHandler(String savePath, AfterCrashLog afterCrashLog) {
        this(savePath);
        this.mAfterCrashLog = afterCrashLog;
    }

    public CrashLogHandler(String savePath) {
        this.mSavePath = savePath;
        if (TextUtils.isEmpty(mSavePath)) {
            throw new RuntimeException(" savePath is null");
        }
    }

    public void setAfterCrashLog(AfterCrashLog afterCrashLog) {
        mAfterCrashLog = afterCrashLog;
    }

    @Override
    public void uncaughtException(@NonNull Thread t, final @NonNull Throwable e) {
        final boolean isMainThread = t == Looper.getMainLooper().getThread();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_", Locale.getDefault());
        final String name = sdf.format(new Date(System.currentTimeMillis())) + t.getName();
        if (isMainThread) {
            writeToFile(name, e);
            if (mAfterCrashLog != null) {
                mAfterCrashLog.crashMainThread();
            }
        } else {
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    writeToFile(name, e);
                    if (mAfterCrashLog != null) {
                        mAfterCrashLog.crashChildThread();
                    }
                }
            });
        }
    }

    private void writeToFile(String fileName, Throwable e) {
        File dir = new File(mSavePath, DIR_NAME);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "文件夹创建失败，可能没有读写权限 ：" + mSavePath);
                return;
            }
        }
        File file = new File(dir, fileName);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Log.e(TAG, "文件创建失败，可能没有读写权限 ：" + fileName);
                    return;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(getStackTraceInfo(e));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取最初Throwable的栈信息
     */
    private String getStackTraceInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        Throwable cause = ex;
        while (cause != null) {
            //异常链
            if (cause.getCause()==null){
                cause.printStackTrace(printWriter);
                break;
            }else {
                cause = cause.getCause();
            }
        }
        printWriter.close();
        return writer.toString();
    }

    public interface AfterCrashLog {
        /**
         * 主线程crash
         */
        void crashMainThread();

        /**
         * 子线程crash
         */
        void crashChildThread();
    }
}
