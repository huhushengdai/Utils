package com.huhushengdai.firework.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huhushengdai.firework.R;
import com.huhushengdai.firework.TextBean;
import com.huhushengdai.tool.view.recycler.BaseRecycleAdapter;
import com.huhushengdai.tool.view.recycler.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Date： 2020/12/21
 * Description:
 *
 * @version 1.0
 */
public class TextAdapter extends BaseRecycleAdapter<TextBean> {

    private List<Animator> animList = new ArrayList<>();

    private int layoutId = R.layout.item_text;

    private OnAniEndListener mOnAniEndListener;
    private Typeface tf;

    public TextAdapter(List<TextBean> data, Context context, Typeface tf) {
        super(context, data);
        //根据路径得到Typeface
        this.tf = tf;
    }

    public TextAdapter(List<TextBean> data, Context context, Typeface tf, int layoutId) {
        this(data, context, tf);
        //根据路径得到Typeface
        this.layoutId = layoutId;
    }

    @Override
    public void afterCreateViewHolder(BaseViewHolder holder) {
        super.afterCreateViewHolder(holder);
        TextView textView = holder.itemView.findViewById(R.id.item_text);
        textView.setTypeface(tf);
    }

    @Override
    public void onViewRefresh(@NonNull BaseViewHolder holder, int position) {
        final TextBean bean = getItemData(position);
        if (bean.isPlay()) {
            return;
        }
        if (position == 0) {
            animList.clear();
        }
        bean.setPlay(true);
        final TextView textView = holder.itemView.findViewById(R.id.item_text);
        textView.setText(bean.content);
        textView.setAlpha(0);
        Animator animator = ObjectAnimator.ofFloat(textView, "alpha", bean.startAlpha, 1f);
        animator.setDuration(bean.delayShowTime);
        animList.add(animator);
        if (position == getItemCount() - 1 && !animList.isEmpty()) {
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animList);
            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mOnAniEndListener != null) {
                        mOnAniEndListener.onAnimationEnd();
                    }
                }
            });
            animSet.start();

        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return layoutId;
    }

    public void setOnAniEndListener(OnAniEndListener listener) {
        mOnAniEndListener = listener;
    }

    public interface OnAniEndListener {
        void onAnimationEnd();
    }
}
