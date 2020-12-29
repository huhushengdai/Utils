package com.huhushengdai.tool.view.recycler;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Date： 2020/7/15
 * Description:
 *
 * @version 1.0
 */
public abstract class BaseRecycleAdapter<T> extends AbsRecycleAdapter<T, BaseViewHolder> {

    public BaseRecycleAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public BaseViewHolder createHolder(View itemView) {
        return new BaseViewHolder(itemView);
    }
}
