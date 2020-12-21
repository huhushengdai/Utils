package com.huhushengdai.firework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {

    private TextView textView;

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
        textView.setText("Merry Christmas");
    }

    public void toStart(View view){
        startActivity(new Intent(this,MainActivity.class));
        finish();
        overridePendingTransition(R.anim.entry, R.anim.exit);
    }
}