package com.huhushengdai.firework.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huhushengdai.firework.R;


/**
 * Date： 2020/7/21
 * Description:
 *
 * @version 1.0
 */
public class AskDialog extends Dialog {

    private TextView mHintText;
    private TextView mLeftView;
    private TextView mRightView;

    private View mContainerView;

    public AskDialog(@NonNull Context context) {
        this(context, R.style.custom_dialog);
    }

    public AskDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        AssetManager mgr = context.getAssets();
        Typeface tf2 = Typeface.createFromAsset(mgr, "hk.ttf");
        setContentView(R.layout.dialog_ask);
        mHintText = findViewById(R.id.askTitle);
        mContainerView = findViewById(R.id.askContainer);
        mHintText.setTypeface(tf2);
        mLeftView = findViewById(R.id.askLeft);
        mLeftView.setTypeface(tf2);
        mRightView = findViewById(R.id.askRight);
        mRightView.setTypeface(tf2);
        setWidth();
    }

    public View getContainerView(){
        return mContainerView;
    }

    private void setWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            Point point = new Point();
            getWindow().getWindowManager().getDefaultDisplay().getSize(point);
            params.width = (int) (point.x * 0.72);
            window.setAttributes(params);
        }
    }

    public void setHintText(String text) {
        mHintText.setText(text);
    }

    public void setLeftOnClickListener(View.OnClickListener listener) {
        mLeftView.setOnClickListener(listener);
    }

    public void setRightOnClickListener(View.OnClickListener listener) {
        mRightView.setOnClickListener(listener);
    }
}
