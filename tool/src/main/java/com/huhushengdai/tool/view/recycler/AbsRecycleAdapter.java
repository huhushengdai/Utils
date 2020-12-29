package com.huhushengdai.tool.view.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huhushengdai.tool.R;

import java.util.List;

/**
 * Date： 2020/7/15
 * Description:
 *
 * @version 1.0
 */
public abstract class AbsRecycleAdapter<T,VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

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

    public AbsRecycleAdapter(Context context, List<T> data) {
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
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(getItemLayoutId(viewType), parent, false);
        VH holder = createHolder(view);
        afterCreateViewHolder(holder);
        return holder;
    }

    public abstract VH createHolder(View itemView);

    protected Context getContext() {
        return mContext;
    }

    public void afterCreateViewHolder(VH holder) {
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
}
