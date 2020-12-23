package com.huhushengdai.firework;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {

    private TextView textView;

    private MediaPlayer mMediaPlayer;

    private ImageView christmasMan;
    private ImageView christmasTree;
    private ImageView gift1;
    private ImageView gift2;
    private ImageView gift3;
    private ImageView gift4;
    private ImageView gift5;
    private ImageView gift6;
    private ImageView gift7;
    private ImageView forGift;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        textView = findViewById(R.id.text);
        //从asset 读取字体
        //得到AssetManager
        AssetManager mgr = getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "Banyu_langits.ttf");
//        Typeface tf = Typeface.createFromAsset(mgr, "Dear_Sunshine.ttf");
        //设置字体
        textView.setTypeface(tf);
//        textView.setText("Merry Christmas");
        //播放音乐
        mMediaPlayer = MediaPlayer.create(this, R.raw.merry_christmas);
        mMediaPlayer.start();
        //
        christmasMan = findViewById(R.id.christmasMan);
        christmasTree = findViewById(R.id.christmasTree);
        gift1 = findViewById(R.id.gift1);
        gift2 = findViewById(R.id.gift2);
        gift3 = findViewById(R.id.gift3);
        gift4 = findViewById(R.id.gift4);
        gift5 = findViewById(R.id.gift5);
        gift6 = findViewById(R.id.gift6);
        gift7 = findViewById(R.id.gift7);
        forGift = findViewById(R.id.forGift);
        showTree();
    }

    private void showTree(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                christmasTree.setVisibility(View.VISIBLE);
                showGift();
            }
        },2000);
    }

    private void showMan(){
//        christmasMan.setAlpha(0);
        christmasMan.setVisibility(View.VISIBLE);
        ValueAnimator animator = ObjectAnimator.ofFloat(christmasMan,"alpha",0,1);
        animator.setDuration(300);
        ValueAnimator animatorText = ObjectAnimator.ofFloat(textView,"alpha",0,1);
        animatorText.setDuration(2000);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animator,animatorText);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showForGift();
            }
        });
        animSet.start();
    }

    private void showForGift(){
       ValueAnimator anim1 = ObjectAnimator.ofFloat(forGift,"translationY",-300,0);
       ValueAnimator anim2 = ObjectAnimator.ofFloat(forGift,"alpha",0,1);
       ValueAnimator anim3 = ObjectAnimator.ofFloat(forGift,"scaleY",0.8f,1);
       ValueAnimator anim4 = ObjectAnimator.ofFloat(forGift,"scaleX",0.8f,1);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(anim1,anim2,anim3,anim4);
        animSet.setDuration(3000);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                textView.setText("希望你喜欢");
                ValueAnimator anim3 = ObjectAnimator.ofFloat(forGift,"scaleY",1,0.9f,1,1.1f,1);
                anim3.setRepeatMode(ValueAnimator.RESTART);
                anim3.setRepeatCount(ValueAnimator.INFINITE);
                ValueAnimator anim4 = ObjectAnimator.ofFloat(forGift,"scaleX",1,0.9f,1,1.1f,1);
                anim4.setRepeatMode(ValueAnimator.RESTART);
                anim4.setRepeatCount(ValueAnimator.INFINITE);
                AnimatorSet animSet = new AnimatorSet();
                animSet.playTogether(anim3,anim4);
                animSet.setDuration(2000);
                animSet.start();
            }
        });
        animSet.start();
    }

    private void showGift(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gift1.setVisibility(View.VISIBLE);
            }
        },1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gift2.setVisibility(View.VISIBLE);
            }
        },2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gift3.setVisibility(View.VISIBLE);
            }
        },3000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gift4.setVisibility(View.VISIBLE);
            }
        },3500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gift5.setVisibility(View.VISIBLE);
            }
        },4000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gift6.setVisibility(View.VISIBLE);
            }
        },4500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gift7.setVisibility(View.VISIBLE);
                showMan();
            }
        },5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeMedia();
    }

    private void closeMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void toStart(View view) {
        closeMedia();
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.entry, R.anim.exit);
    }
}