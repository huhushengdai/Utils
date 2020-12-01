package com.huhushengdai.utilsdemo.log;

import android.os.Handler;
import android.os.Looper;

import com.huhushengdai.log.LogHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Date： 2020/12/1
 * Description:
 * 把日志写入文件
 *
 * @version 1.0
 */
public class LogWriteFileHandler implements LogHandler {
    /**
     * 文件保存路径
     */
    private String mSavePath;

    /**
     * 文件写入文件里面，该文件允许最大大小
     */
    private long mSingleFileMaxSize;

    /**
     * 文件夹最大容量大小
     * 当超出的时候，删除时间最久远的文件
     */
    private long mDirMaxSize;

    /**
     * 由于有大量读写操作，非常耗时
     * 所以创建一个子线程handler，去做操作
     */
    private Handler mHandler;

    private FileWriter mFileWriter;

    public LogWriteFileHandler() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Looper looper = Looper.myLooper();
                if (looper == null) {
                    throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
                }
                mHandler = new Handler(looper);
                Looper.loop();
            }
        });
        thread.setName("LogWriteThread" + thread.hashCode());
        thread.start();
    }

    @Override
    public void onLog(String threadName, String fileName, int lineNum, String msg, int level) {
        if (mHandler == null) {
            return;
        }
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private FileWriter getFileWriter() {
        if (mFileWriter != null) {
            return mFileWriter;
        }
        File writeFile = getFile();
        if (writeFile == null) {
            return null;
        }
        try {
            mFileWriter = new FileWriter(writeFile, true);
            return mFileWriter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private File getFile() {
        File dirs = new File(mSavePath);
        if (!dirs.exists()) {
            if (!dirs.mkdirs()) {
                return null;
            }
        }
        File[] files = dirs.listFiles();
        File lastFile = null;//最后创建的文件
        if (files != null) {
            File tempFile;
            for (int i = 0, size = files.length; i < size; i++) {
                tempFile = files[i];
                if (!tempFile.isFile()) {
                    continue;
                }
                try {
                    //以时间戳作为文件名
                    //比较文件名时间戳时间，找出最后创建的文件
                    long tempName = Long.parseLong(tempFile.getName());
                    if (lastFile == null) {
                        lastFile = tempFile;
                        continue;
                    }
                    long lastName = Long.parseLong(lastFile.getName());
                    if (tempName > lastName) {
                        lastFile = tempFile;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (lastFile == null || lastFile.length() > mSingleFileMaxSize) {
            //没有存储日志的文件，或者该文件的大小已经超过
            //创建一个新的文件
            lastFile = new File(mSavePath, System.currentTimeMillis() + "");
            try {
                if (!lastFile.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return lastFile;
    }

    private final class CloseStreamTask implements Runnable {

        @Override
        public void run() {
            if (mFileWriter == null) {
                return;
            }
            try {
                mFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mFileWriter = null;
        }
    }
}
