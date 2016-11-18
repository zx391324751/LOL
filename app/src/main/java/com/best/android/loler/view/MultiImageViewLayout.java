package com.best.android.loler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/11/17.
 */

public class MultiImageViewLayout extends FrameLayout{

    private List<View>listChild;

    public MultiImageViewLayout(Context context) {
        super(context);
    }

    public MultiImageViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiImageViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        init();
    }

    private void init() {
        listChild = new ArrayList<View>();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

}
