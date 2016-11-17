package com.best.android.loler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by BL06249 on 2016/1/7.
 */
public class SquareImageView extends ImageView {

    private Context context;
    private float weight = 1/6f;

    public SquareImageView(Context context) {
        super(context);
        this.context = context;
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = (int)(outMetrics.widthPixels * weight);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int leftMargin = lp.leftMargin;
        int rightMargin = lp.rightMargin;
        int topMargin = lp.topMargin;
        int bottomMargin = lp.bottomMargin;
        setMeasuredDimension(width - leftMargin - rightMargin, width - topMargin - bottomMargin);
    }
}
