package com.huhushengdai.utilsdemo.bean;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Date： 2021/3/11
 * Description:
 */
public class UserBean {
    public final MutableLiveData<String> name = new MutableLiveData<>();
}
