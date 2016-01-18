package com.best.android.loler.view;

import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

/**
 * Created by BL06249 on 2015/12/16.
 */
public class ScrollerLinearLayout extends LinearLayout {

    private View centerView = null; //不滑动显示的view
    private View rightView = null; //左滑显示的view

    //用这两个可以实现滑动效果
    private ScrollerCompat mOpenScroller;
    private ScrollerCompat mCloseScroller;

    private int downX; //开始按下的位置

    //记录状态
    private int state = STATE_CLOSE;
    private static final int STATE_CLOSE = 0;
    private static final int STATE_OPEN = 1;

    private int mBaseX;

    public ScrollerLinearLayout(Context context) {
        super(context);
        //初始化mColoseScroller和mOpenScroller
        mCloseScroller = ScrollerCompat.create(getContext());
        mOpenScroller = ScrollerCompat.create(getContext());
    }

    public ScrollerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCloseScroller = ScrollerCompat.create(getContext());
        mOpenScroller = ScrollerCompat.create(getContext());
    }

    public ScrollerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCloseScroller = ScrollerCompat.create(getContext());
        mOpenScroller = ScrollerCompat.create(getContext());
    }

    public void setContentView(View centerView, View rightView){
        this.centerView = centerView;
        this.rightView = rightView;

        //初始化mColoseScroller和mOpenScroller
        mCloseScroller = ScrollerCompat.create(getContext());
        mOpenScroller = ScrollerCompat.create(getContext());
        initView();
    }

    //child view的布局参数设定好后 添加到parent view里面
    private void initView() {

        setLayoutParams(new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        //这也是设置宽和高
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        centerView.setLayoutParams(contentParams);
        rightView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        //将这两个布局都add到这个view中
        addView(centerView);
        addView(rightView);
    }

    // 判断是否滑出的状态
    public boolean isOpen() {
        return state == STATE_OPEN;
    }

    /**
     * 这里的函数是由 listview来控制的。
     * listview来监听左滑，并判断是否需要将rightview 显示出来。
     * （因为不是所有左滑都要画出rightview，还需要判断listview的其他item的状态不是STATE_OPEN 状态
     * ，所以由listview来控制自己item的rightview是否左滑最合理，这个函数目的就是让listview来调用）
     * 反之右滑亦然
     *
     * @param event
     * @return
     */
    public boolean onSwipeLeft(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int dis = (int) (downX - event.getX());
                if (state == STATE_OPEN) {
                    dis += rightView.getWidth();
                }
                //手指移动多少距离，rightview也移动多少距离
                swipe(dis);
                break;
            case MotionEvent.ACTION_UP:
                if ((downX - event.getX()) > (rightView.getWidth() / 2)) {
                    smoothOpenMenu(); //自动滑出来
                } else {
                    smoothCloseMenu(); //自动滑进去
                    return false;
                }
                break;
        }
        //消费掉事件
        return true;
    }

    @Override
    public void computeScroll() {
        if (state == STATE_OPEN) {
            if (mOpenScroller.computeScrollOffset()) {
                swipe(mOpenScroller.getCurrX());
                postInvalidate();
            }
        } else {
            if (mCloseScroller.computeScrollOffset()) {
                swipe(mBaseX - mCloseScroller.getCurrX());
                postInvalidate();
            }
        }
    }

    private void swipe(int dis) {
        if (dis > rightView.getWidth()) {
            dis = rightView.getWidth();
        }
        if (dis < 0) {
            dis = 0;
        }
        centerView.layout(-dis, centerView.getTop(), centerView.getWidth() - dis, getMeasuredHeight());
        rightView.layout(centerView.getWidth() - dis, rightView.getTop(), centerView.getWidth() + rightView.getWidth() - dis, rightView.getBottom());
    }

    public void smoothCloseMenu() {
        state = STATE_CLOSE;
        mBaseX = -centerView.getLeft();
        mCloseScroller.startScroll(0, 0, mBaseX, 0, 350);
        postInvalidate();
    }
    public void smoothOpenMenu() {
        state = STATE_OPEN;
        mOpenScroller.startScroll(-centerView.getLeft(), 0, rightView.getWidth(), 0, 350);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(rightView != null)
            rightView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(centerView != null)
            centerView.layout(0, 0, getMeasuredWidth(), centerView.getMeasuredHeight());
        if(rightView != null)
            rightView.layout(getMeasuredWidth(), 0, getMeasuredWidth() + rightView.getMeasuredWidth(), centerView.getMeasuredHeight());
    }

}
