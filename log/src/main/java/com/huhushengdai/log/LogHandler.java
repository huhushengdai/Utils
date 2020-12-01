package com.huhushengdai.log;

/**
 * Date： 2020/12/1
 * Description:
 * 处理日志的接口，比如打印、保存到本地、上传到服务器灯
 *
 * @version 1.0
 */
public interface LogHandler {
    /**
     * 处理日志
     *
     * @param threadName 调用时线程名称
     * @param fileName   文件名
     * @param lineNum    行号
     * @param msg        打印信息
     * @param level      日志等级
     */
    void onLog(String threadName, String fileName, int lineNum, String msg, int level);
}
