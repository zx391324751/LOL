package com.best.android.loler.aaTest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.best.android.loler.R;

/**
 * Created by BL06249 on 2015/12/29.
 */
public class ScrollerTitleLayout extends RelativeLayout {

    private View titleView;
    private View contentView;
    private View toolbarView;

    private int parentHeight;
    private int parentWidth;
    private int titleViewMinHeight = 200;
    private int titleViewHeight;
    private int contentViewHeight;

    private int contentViewTop;
    private int state;
    private final int UP_STATE = -1;
    private final int MOVE_STATE = 0;
    private final int DOWN_STATE = 1;

    private float downY;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(titleView == null || contentView == null){
            init();
            state = DOWN_STATE;
        }
    }

    public ScrollerTitleLayout(Context context) {
        super(context);
    }

    public ScrollerTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollerTitleLayout);
        titleViewMinHeight = (int) typeArray.getDimension(R.styleable.ScrollerTitleLayout_minTitleViewHeader, 200);
    }

    public ScrollerTitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(contentView != null && titleView != null){
            titleView.layout(0, contentViewTop - titleViewHeight, parentWidth, titleViewHeight);
            contentView.layout(0, contentViewTop, parentWidth, parentHeight);

//            float percent = 1 - (contentViewTop - titleViewMinHeight)* 1.0f/(titleViewHeight - titleViewMinHeight);
//            int alpha = (int) (percent * 255);
//            toolbarView.setBackgroundColor(Color.argb(alpha, 11, 99, 240));
        }
    }

    private void init() {
        titleView = (View)getChildAt(0);
        if(titleView != null) {
            titleViewHeight = titleView.getHeight();
            contentViewTop = titleViewHeight;
        }
        contentView = (View)getChildAt(1);
        if(contentView != null) {
            contentViewHeight = contentView.getHeight();
            parentWidth = contentView.getWidth();
        }
        toolbarView = (View)getChildAt(2);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveLen = (int) (ev.getY() - downY);
                //小于0  move的方向是up , 大于0 move的方向是down
                if(moveLen < 0){
                    if(contentViewTop <= titleViewMinHeight) {
                        return false;
                    } else {
                        return true;
                    }
                } else if(moveLen > 0){
                    if(contentView instanceof ScrollView){
                        int scrollY = contentView.getScrollY();
                        if(scrollY == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void saveState() {
        if(contentViewTop > (titleViewHeight + titleViewMinHeight) / 2){
            contentViewTop = titleViewHeight;
            state = DOWN_STATE;
        } else {
            contentViewTop = titleViewMinHeight;
            state = UP_STATE;
        }
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                int moveLen = (int) (event.getY() - downY);
                onScale(moveLen);
                break;
            case MotionEvent.ACTION_UP:
                saveState();
                break;
        }
        return false;
    }

    private void onScale(int len){
        if(state == DOWN_STATE)
            contentViewTop = titleViewHeight + len;
        if(state == UP_STATE)
            contentViewTop = titleViewMinHeight + len;
        if(contentViewTop < titleViewMinHeight)
            contentViewTop = titleViewMinHeight;
        if(contentViewTop > titleViewHeight)
            contentViewTop = titleViewHeight;
        requestLayout();
    }
}
