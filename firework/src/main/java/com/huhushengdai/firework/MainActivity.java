package com.huhushengdai.firework;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huhushengdai.firework.adapter.TextAdapter;
import com.huhushengdai.firework.email.EmailUtils;
import com.huhushengdai.firework.widget.AskDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "anim";
    private ImageView img;

    private int mScreenWidth;
    private int mScreenHeight;

    private String info = "Receiver收到PUBLISH包以后，向Sender发送一个PUBACK数据包，PUBACK数据包没有消息体（Payload），在可变头中有一个包标识（Packet Identifier），和它收到的PUBLISH包中的Packet Identifier一致。";
    private Handler handler = new Handler();

    private int index = 1;

    private TextAdapter mTextAdapter;
    private TextAdapter mTextAdapter2;
    private TextAdapter mTextAdapter3;
    private TextAdapter mTextAdapter4;
    private TextAdapter mTextAdapter5;
    private TextAdapter mTextAdapter6;

    private TextView author;

    private boolean animFinish;

    private View mContainer;

    private boolean toFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        Display display = getWindowManager().getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
        mContainer = findViewById(R.id.container);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    o2();
                } else {
                    enlarge(1000);
                }
                flag = !flag;
            }
        });
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fz.ttf");
        mTextAdapter = new TextAdapter(null, this, tf);
        mTextAdapter.setOnAniEndListener(new TextAdapter.OnAniEndListener() {
            @Override
            public void onAnimationEnd() {
                showText2();
            }
        });
        initRecycler((RecyclerView) findViewById(R.id.textList), mTextAdapter);

        RecyclerView textList2 = findViewById(R.id.textList2);
        mTextAdapter2 = new TextAdapter(null, this, tf);
        mTextAdapter2.setOnAniEndListener(new TextAdapter.OnAniEndListener() {
            @Override
            public void onAnimationEnd() {
                showText3();
            }
        });
        initRecycler(textList2, mTextAdapter2);

        mTextAdapter3 = new TextAdapter(null, this, tf);
        mTextAdapter3.setOnAniEndListener(new TextAdapter.OnAniEndListener() {
            @Override
            public void onAnimationEnd() {
                showText4();
            }
        });
        initRecycler((RecyclerView) findViewById(R.id.textList3), mTextAdapter3);

        mTextAdapter4 = new TextAdapter(null, this, tf);
        mTextAdapter4.setOnAniEndListener(new TextAdapter.OnAniEndListener() {
            @Override
            public void onAnimationEnd() {
                img.setVisibility(View.VISIBLE);
                o();
            }
        });
        initRecycler((RecyclerView) findViewById(R.id.textList4), mTextAdapter4);

        Typeface tf2 = Typeface.createFromAsset(mgr, "hk.ttf");
        mTextAdapter5 = new TextAdapter(null, this, tf2, R.layout.item_text2);
        mTextAdapter5.setOnAniEndListener(new TextAdapter.OnAniEndListener() {
            @Override
            public void onAnimationEnd() {
                showText6();
            }
        });
        initRecycler((RecyclerView) findViewById(R.id.textList5), mTextAdapter5);


        mTextAdapter6 = new TextAdapter(null, this, tf2, R.layout.item_text2);
        mTextAdapter6.setOnAniEndListener(new TextAdapter.OnAniEndListener() {
            @Override
            public void onAnimationEnd() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(author, "alpha", 0, 1);
                        alphaAnimator.setDuration(2000);
                        alphaAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                Toast.makeText(MainActivity.this, "over,按返回键可以退出", Toast.LENGTH_SHORT).show();
                                animFinish = true;
                            }
                        });
                        alphaAnimator.start();
                    }
                }, 2000);

            }
        });
        initRecycler((RecyclerView) findViewById(R.id.textList6), mTextAdapter6);

        author = findViewById(R.id.author);
        author.setTypeface(tf);
    }

    private AskDialog askDialog;

    private void showAskDialog() {
        if (askDialog == null) {
            askDialog = new AskDialog(this);
            askDialog.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toFinish) {
                        return;
                    }
                    EmailUtils.send("lu so sad");
                    askDialog.setHintText("so sad，see you");
                    toFinish();
                }
            });
            askDialog.setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toFinish) {
                        return;
                    }
                    EmailUtils.send("lu so good");
                    askDialog.setHintText("就知道你会喜欢，See you");
                    toFinish();
                }
            });
        }
        askDialog.show();
    }

    private void toFinish() {
        toFinish = true;
        ValueAnimator alphaAnimator2 = ObjectAnimator.ofFloat(askDialog.getContainerView(), "alpha", 1, 0);
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(mContainer, "alpha", 1, 0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(alphaAnimator).with(alphaAnimator2);
        animSet.setDuration(5000);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (askDialog != null && askDialog.isShowing()) {
                    askDialog.dismiss();
                }
                finish();
                overridePendingTransition(R.anim.entry, R.anim.exit);
            }
        });
        animSet.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (animFinish) {
                showAskDialog();
            } else {
                Toast.makeText(this, "稍等片刻，还有哦...", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initRecycler(RecyclerView recyclerView, TextAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    boolean first = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (first) {
            first = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enlarge(0);
                    flag = false;
                }
            }, 50);
            showText1();
        }
    }

    private void showText1() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextAdapter.setData(getTextBean("仿佛兮", 1200));
                mTextAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    private void showText2() {
        mTextAdapter2.setData(getTextBean("若轻云之蔽月", 700));
        mTextAdapter2.notifyDataSetChanged();
    }

    private void showText3() {
        mTextAdapter3.setData(getTextBean("飘摇兮", 1000));
        mTextAdapter3.notifyDataSetChanged();
    }

    private void showText4() {
        mTextAdapter4.setData(getTextBean("若流风之回雪", 800));
        mTextAdapter4.notifyDataSetChanged();
    }

    private void showText5() {
        mTextAdapter5.setData(getTextBean("（送给最美的小仙女", 600));
        mTextAdapter5.notifyDataSetChanged();
    }

    private void showText6() {
        mTextAdapter6.setData(getTextBean("                      喜欢么）", 30));
        mTextAdapter6.notifyDataSetChanged();
    }

    private List<TextBean> getTextBean(String text, long delayTime) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        List<TextBean> list = new ArrayList<>();
        char[] chars = text.toCharArray();
        for (int i = 0, size = chars.length; i < size; i++) {
            list.add(new TextBean(String.valueOf(chars[i]), (i + 1) * delayTime, 0));
        }
        return list;
    }

    boolean flag;

    private void o() {

        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(img, "alpha", 0, 1);
        alphaAnimator.setDuration(2000);
        ValueAnimator scaleAnimator = ObjectAnimator.ofFloat(img, "scaleX", 1);
        ValueAnimator scaleAnimatorY = ObjectAnimator.ofFloat(img, "scaleY", 1);
        ValueAnimator tranceAnimator = ObjectAnimator.ofFloat(img, "translationX", 0);
        ValueAnimator tranceAnimatorY = ObjectAnimator.ofFloat(img, "translationY", 0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleAnimator)
                .after(alphaAnimator).with(tranceAnimator).with(tranceAnimatorY).with(scaleAnimatorY);
        animSet.setDuration(4000);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showText5();
            }
        });
        animSet.start();
    }

    private void o2() {


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
        float translationY = img.getY() - mScreenHeight / 2f + img.getHeight() / 2f;
        ValueAnimator tranceAnimatorY = ObjectAnimator.ofFloat(img, "translationY", -translationY);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleAnimator).with(tranceAnimator).with(scaleAnimatorY).with(tranceAnimatorY);
        animSet.setDuration(time);
        animSet.start();
    }
}
