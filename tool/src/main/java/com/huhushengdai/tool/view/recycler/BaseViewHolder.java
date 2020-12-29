package com.huhushengdai.tool.view.recycler;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huhushengdai.tool.R;

/**
 * Date： 2020/12/29
 * Description:
 *
 * @version 1.0
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private AbsRecycleAdapter.OnChildClickListener mListener;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setOnChildClickListener(AbsRecycleAdapter.OnChildClickListener listener) {
        mListener = listener;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    public void setClickChild(@IdRes int childViewId) {
        View child = itemView.findViewById(childViewId);
        if (child != null) {
            child.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        mListener.onChildClick(itemView, v, (Integer) itemView.getTag(R.id.item_position));
    }
}
