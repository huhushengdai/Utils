package com.huhushengdai.utilsdemo;

import androidx.databinding.DataBindingComponent;

/**
 * Date： 2021/3/9
 * Description:
 */
public class MyBindingComponent implements DataBindingComponent {
    @Override
    public MyBinding getMyBinding() {
        return new MyBinding();
    }
}
