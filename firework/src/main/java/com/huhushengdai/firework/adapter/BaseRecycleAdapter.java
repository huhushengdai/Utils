package com.huhushengdai.firework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.huhushengdai.firework.R;

import java.util.List;

/**
 * Date： 2020/7/15
 * Description:
 *
 * @version 1.0
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.BaseViewHolder> {

    private List<T> mData;
    private Context mContext;
    private OnChildClickListener mSetListener;
    private OnChildClickListener mSelfListener = new OnChildClickListener() {
        @Override
        public void onChildClick(View parent, View child, int position) {
            if (mSetListener != null) {
                mSetListener.onChildClick(parent, child, position);
            }
        }
    };

    public BaseRecycleAdapter(List<T> data, Context context) {
        this.mData = data;
        this.mContext = context;
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        mSetListener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder holder = createHolder(parent, viewType);
        afterCreateViewHolder(holder);
        return holder;
    }

    public BaseViewHolder createHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(getItemLayoutId(viewType), parent, false);
        return new BaseViewHolder(view);
    }

    protected Context getContext() {
        return mContext;
    }

    public void afterCreateViewHolder(BaseViewHolder holder) {
        holder.setOnChildClickListener(mSelfListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.itemView.setTag(R.id.item_position, position);
        onViewRefresh(holder, position);
    }

    public T getItemData(int position) {
        return mData == null ? null : mData.get(position);
    }

    public abstract void onViewRefresh(@NonNull BaseViewHolder holder, int position);

    protected abstract int getItemLayoutId(int viewType);

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnChildClickListener {
        void onChildClick(View parent, View child, int position);
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnChildClickListener mListener;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void setOnChildClickListener(OnChildClickListener listener) {
            mListener = listener;
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
}
