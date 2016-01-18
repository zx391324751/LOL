package com.best.android.loler.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by BL06249 on 2016/1/6.
 */
public class SkillIndicatorLayout extends LinearLayout {

    private Context context;
    private Paint paint;
    private int screenWidth;
    private final int CHILD_VIEW_COUNT = 5;
    private int mHeight;

    private int mLeft; // 指示符的left
    private int currentPosition;
    private boolean isScrolling;
    private OnSelectedChangedListener onSelectedChangedListener;

    public interface OnSelectedChangedListener{
        public void changed(int position);
    }

    public SkillIndicatorLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SkillIndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SkillIndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init(){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        mLeft = 0;
        currentPosition = 0;
        isScrolling = false;
    }

    public void addOnSelectedChangedListener(OnSelectedChangedListener onSelectedChangedListener){
        this.onSelectedChangedListener = onSelectedChangedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = screenWidth;
        int height = screenWidth / CHILD_VIEW_COUNT;
        mHeight = height;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect(mLeft, 0, mLeft + mHeight, mHeight);
        canvas.drawRect(rect, paint); // 绘制该矩形
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            int position = (int)(x / (screenWidth / CHILD_VIEW_COUNT));
            moveSmooth(position);
        }
        return super.onTouchEvent(event);
    }

    public void moveSmooth(int position){
        if(position == currentPosition || isScrolling) {
            return;
        }
        if(onSelectedChangedListener != null)
            onSelectedChangedListener.changed(position);
        isScrolling = true;
        currentPosition = position;
        float startX = mLeft;
        float endX = (screenWidth / CHILD_VIEW_COUNT) * position;
        ValueAnimator animator = ValueAnimator.ofFloat(startX, endX);
        animator.setDuration(500);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (Float) animation.getAnimatedValue();
                mLeft = (int)val;
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScrolling = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isScrolling = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}
