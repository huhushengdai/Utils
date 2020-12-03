package com.huhushengdai.utilsdemo;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.huhushengdai.crash.CrashLogHandler;

/**
 * Date： 2020/12/3
 * Description:
 *
 * @version 1.0
 */
public class BaseApp extends Application {

    private CrashLogHandler.AfterCrashLog afterCrashLog = new CrashLogHandler.AfterCrashLog() {
        @Override
        public void crashMainThread() {
            Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            if (intent == null) {
                return;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }

        @Override
        public void crashChildThread() {
            Log.e("BaseApp", "子线程崩溃");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        String path = Environment.getExternalStorageDirectory() + "/Download";
        CrashLogHandler crashLogHandler = new CrashLogHandler(path, afterCrashLog);
        Thread.setDefaultUncaughtExceptionHandler(crashLogHandler);
    }
}
