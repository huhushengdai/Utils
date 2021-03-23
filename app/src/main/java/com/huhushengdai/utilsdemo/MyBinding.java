package com.huhushengdai.utilsdemo;

import android.view.View;

import androidx.databinding.BindingAdapter;

/**
 * Date： 2021/3/9
 * Description:
 */
public class MyBinding {

    @BindingAdapter("userInfo")
    public void setUser(View view, boolean userInfo) {
        view.setBackgroundColor(view.getContext().getResources().getColor(userInfo ? android.R.color.darker_gray : android.R.color.holo_red_dark));
    }
}
