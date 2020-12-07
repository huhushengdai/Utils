package com.huhushengdai.tool.log;

import android.util.Log;

import com.huhushengdai.tool.log.utils.MsgUtils;

/**
 * Date： 2020/12/1
 * Description:
 * 默认处理log的方式
 * 这里默认用系统log打印信息
 *
 * @version 1.0
 */
public class DefaultLogHandler implements LogHandler {
    private static final String TAG = "LogHandler";


    @Override
    public void onLog(String threadName, String fileName, String methodName, int lineNum, String msg, int level) {
        String log = MsgUtils.getMsg(threadName, fileName, methodName, lineNum, msg);
        switch (level) {
            default:
            case LogTool.DEBUG:
                Log.d(TAG, log);
                break;
            case LogTool.INFO:
                Log.i(TAG, log);
                break;
            case LogTool.WARN:
                Log.w(TAG, log);
                break;
            case LogTool.ERROR:
                Log.e(TAG, log);
                break;
        }
    }
}
