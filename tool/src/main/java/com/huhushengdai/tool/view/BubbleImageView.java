package com.huhushengdai.tool.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.huhushengdai.tool.R;


/**
 * Date： 2020/12/22
 * Description:
 * 遮罩view，可以让image显示成遮罩层的形状
 * 注意遮罩层 和 显示内容 必须是bitmap，在res中需要放在drawable里面，放在mipmap里面不会有效果
 *
 * @version 1.0
 */
public class BubbleImageView extends AppCompatImageView {

    private Drawable bgDrawable;

    public BubbleImageView(Context context) {
        this(context, null);
    }

    public BubbleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Overlap, defStyle, 0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Overlap_dstBg) {
                bgDrawable = a.getDrawable(attr);
            }
        }
        a.recycle();
    }

    @Override//图片缩放还未处理
    public void draw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        if (!(getDrawable() instanceof BitmapDrawable) || bgDrawable == null) {
            super.draw(canvas);
            return;
        }
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        Bitmap srcBitmap = drawable.getBitmap();//源图片
        if (srcBitmap == null) {
            super.draw(canvas);
            return;
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Paint paint = drawable.getPaint();
        paint.setAntiAlias(true);

        paint.setXfermode(null);
        int sc = canvas.saveLayer(0, 0, width, height
                , paint, Canvas.ALL_SAVE_FLAG);


        bgDrawable.setBounds(0, 0, width, height);
        bgDrawable.draw(canvas);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        float srcWidth = srcBitmap.getWidth();
        float srcHeight = srcBitmap.getHeight();
        float scale;
        if (srcWidth < srcHeight) {
            scale = width / srcWidth;
        } else {
            scale = height / srcHeight;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        canvas.drawBitmap(srcBitmap, matrix, paint);

        canvas.restoreToCount(sc);
    }


}