package com.huhushengdai.firework;

/**
 * Date： 2020/12/21
 * Description:
 *
 * @version 1.0
 */
public class TextBean {
    public final String content;
    public final long delayShowTime;
    public final float startAlpha;
    private boolean play;

    public TextBean(String content, long delayShowTime,float startAlpha) {
        this.content = content;
        this.delayShowTime = delayShowTime;
        this.startAlpha = startAlpha;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }
}
