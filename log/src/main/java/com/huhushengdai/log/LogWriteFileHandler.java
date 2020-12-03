package com.huhushengdai.log;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Date： 2020/12/1
 * Description:
 * 把日志写入文件
 *
 * @version 1.0
 */
public class LogWriteFileHandler implements LogHandler {
    private static final String TAG = LogWriteFileHandler.class.getSimpleName();

    private static final long CLOSE_DELAY_TIME = 3 * 1000;
    /**
     * 文件夹名称
     */
    private static final String DIR_NAME = "log";
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
    private long mCurrentFileSize;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    private LogWriteFileHandler(String savePath, long singleFileMaxSize, long dirMaxSize) {
        mSavePath = savePath;
        mSingleFileMaxSize = singleFileMaxSize;
        mDirMaxSize = dirMaxSize;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Looper looper = Looper.myLooper();
                if (looper == null) {
                    throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
                }
                mHandler = new TaskHandler(looper);
                Looper.loop();
            }
        });
        thread.setName("LogWriteThread" + thread.hashCode());
        thread.start();
    }

    @Override
    public void onLog(final String threadName, final String fileName, final int lineNum, final String msg, final int level) {
        if (mHandler == null) {
            return;
        }
        final long time = System.currentTimeMillis();
        String infoLevel;
        switch (level) {
            case LogTool.DEBUG:
                infoLevel = "debug";
                break;
            case LogTool.INFO:
                infoLevel = "info";
                break;
            case LogTool.WARN:
                infoLevel = "warn";
                break;
            case LogTool.ERROR:
                infoLevel = "error";
                break;
            default:
                infoLevel = "unknow";
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.format(new Date(time)))
                .append(" ").append(infoLevel).append(" ")
                .append(String.format(Locale.CHINA, "[%s] (%s:%d) %s", threadName, fileName, lineNum, msg))
                .append("\n");
        Message message = mHandler.obtainMessage(TaskHandler.HANDLER_WRITE, sb.toString());
        mHandler.sendMessage(message);
    }

    private void writeInfo(String info) {
        FileWriter fileWriter = getFileWriter();
        if (fileWriter == null) {
            Log.e(TAG, "获取OutputStream 失败，请查看是否有读取权限");
            return;
        }
        mHandler.removeMessages(TaskHandler.HANDLER_CLOSE);
        try {
            fileWriter.write(info);
            mCurrentFileSize += info.length();
            if (mCurrentFileSize > mSingleFileMaxSize) {
                closeWriteStream();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessageDelayed(TaskHandler.HANDLER_CLOSE, CLOSE_DELAY_TIME);
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
            mCurrentFileSize = writeFile.length();
            return mFileWriter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private File getFile() {
        File dirs = new File(mSavePath, DIR_NAME);
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
            lastFile = new File(dirs, +System.currentTimeMillis() + "");
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

    /**
     * 检查文件夹的内容是否超过
     * 如果超过了设定，清楚掉最早记录的文件
     */
    private void clearSuperfluous() {
        File file = new File(mSavePath, DIR_NAME);
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        long dirSize = 0;
        for (int i = 0, size = files.length; i < size; i++) {
            dirSize += files[i].length();
        }
        if (dirSize < mDirMaxSize) {
            return;
        }
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                //抛出异常默认不是log文件，优先删除
                long f1;
                try {
                    f1 = Long.parseLong(o1.getName());
                } catch (Exception e) {
                    return -1;
                }
                long f2;
                try {
                    f2 = Long.parseLong(o2.getName());
                } catch (Exception e) {
                    return 1;
                }
                return Long.compare(f1, f2);
            }
        });
        Iterator<File> it = fileList.iterator();
        File temp;
        long tempSize;
        while (dirSize > mDirMaxSize && it.hasNext()) {
            temp = it.next();
            tempSize = temp.length();
            if (!temp.delete()) {
                Log.e(TAG, "文件删除失败:" + temp.getAbsoluteFile());
            } else {
                dirSize -= tempSize;
            }
        }
    }

    private void closeWriteStream() {
        if (mFileWriter == null) {
            return;
        }
        try {
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mFileWriter = null;
        mCurrentFileSize = 0;
        //文件创建成功后，查看下当前文件夹下内容是否超出设置
        //注意清楚多余文件
        mHandler.sendEmptyMessage(TaskHandler.HANDLER_CLEAR);
    }

    private final class TaskHandler extends Handler {
        private static final int HANDLER_CLOSE = 1;//关闭输出流
        private static final int HANDLER_WRITE = 2;//写入信息
        private static final int HANDLER_CLEAR = 3;//清除文件

        public TaskHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_CLEAR:
                    clearSuperfluous();
                    break;
                case HANDLER_WRITE:
                    writeInfo(msg.obj.toString());
                    break;
                case HANDLER_CLOSE:
                    closeWriteStream();
                    break;
            }
        }
    }

    public static class Builder {
        private String savePath;
        private long singleFileMaxSize;
        private long dirMaxSize;

        public Builder setSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public Builder setSingleFileMaxSize(long singleFileMaxSize) {
            this.singleFileMaxSize = singleFileMaxSize;
            return this;
        }

        public Builder setDirMaxSize(long dirMaxSize) {
            this.dirMaxSize = dirMaxSize;
            return this;
        }

        public LogWriteFileHandler build() {
            if (TextUtils.isEmpty(savePath)) {
                throw new RuntimeException("savePath is null");
            }
            if (singleFileMaxSize <= 0) {
                throw new RuntimeException("singleFileMaxSize <= 0");
            }
            if (dirMaxSize <= 0) {
                throw new RuntimeException("dirMaxSize <= 0");
            }
            if (singleFileMaxSize > dirMaxSize) {
                throw new RuntimeException("singleFileMaxSize > dirMaxSize");
            }
            return new LogWriteFileHandler(savePath, singleFileMaxSize, dirMaxSize);
        }
    }

}
