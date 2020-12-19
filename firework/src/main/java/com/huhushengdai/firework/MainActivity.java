package com.huhushengdai.firework;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "anim";
    private ImageView img;
    private TextView textView;

    private int mScreenWidth;
    private int mScreenHeight;

    private String info = "Receiver收到PUBLISH包以后，向Sender发送一个PUBACK数据包，PUBACK数据包没有消息体（Payload），在可变头中有一个包标识（Packet Identifier），和它收到的PUBLISH包中的Packet Identifier一致。";
    private Handler handler = new Handler();

    private int[] r = {R.mipmap.bg, R.mipmap.bg1,  R.mipmap.bg4};
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        textView = findViewById(R.id.text);
        Display display = getWindowManager().getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    o();
                } else {
                    enlarge(1000);
                }
                flag = !flag;
            }
        });
        handler.post(new ShowTextTask(0));
    }

    boolean first = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (first){
            first = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enlarge(0);
                    flag = true;
                }
            },50);
        }
    }

    public void changeBg(View v) {
        v.setBackgroundResource(r[index++ % r.length]);
    }

    private class ShowTextTask implements Runnable {

        private int end;

        public ShowTextTask(int end) {
            this.end = end;
        }

        @Override
        public void run() {
            if (end > info.length()) {
                return;
            }
//            char index = info.charAt(end);
//            while (index >= 'a' && index <= 'z') {
//                end++;
//                if (end > info.length()) {
//                    break;
//                }
//                index = info.charAt(end);
//            }
            String text = info.substring(0, end);
            textView.setText(text);
            handler.postDelayed(new ShowTextTask(end + 1), 150);
        }
    }

    boolean flag;

    private void o() {
        ValueAnimator scaleAnimator = ObjectAnimator.ofFloat(img, "scaleX", 1);
        ValueAnimator scaleAnimatorY = ObjectAnimator.ofFloat(img, "scaleY", 1);
        ValueAnimator tranceAnimator = ObjectAnimator.ofFloat(img, "translationX", 0);
        ValueAnimator tranceAnimatorY = ObjectAnimator.ofFloat(img, "translationY", 0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleAnimator).with(tranceAnimator).with(tranceAnimatorY).with(scaleAnimatorY);
        animSet.setDuration(1000);
        animSet.start();
    }

    private void enlarge(long time) {
        int width = img.getWidth();
        float scale = (float) (mScreenWidth * 0.8 / (float) width);
        ValueAnimator scaleAnimator = ObjectAnimator.ofFloat(img, "scaleX", scale);
        ValueAnimator scaleAnimatorY = ObjectAnimator.ofFloat(img, "scaleY", scale);
        float tanslation = img.getX() - mScreenWidth / 2f + img.getWidth() / 2f;
        ValueAnimator tranceAnimator = ObjectAnimator.ofFloat(img, "translationX", -tanslation);
        float translationY = img.getY()-mScreenHeight/2f +img.getHeight()/2f;
        ValueAnimator tranceAnimatorY = ObjectAnimator.ofFloat(img, "translationY", -translationY);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleAnimator).with(tranceAnimator).with(scaleAnimatorY).with(tranceAnimatorY);
        animSet.setDuration(time);
        animSet.start();
    }
}
