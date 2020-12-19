package com.huhushengdai.firework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void toStart(View view){
        startActivity(new Intent(this,MainActivity.class));
        finish();
        overridePendingTransition(R.anim.entry, R.anim.exit);
    }
}