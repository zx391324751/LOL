package com.best.android.loler.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.best.android.loler.R;
import com.best.android.loler.util.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * activity基类
 * 主要用于所有activity的共同元素的初始化，简化代码
 * 比如标题栏，状态栏，网络加载失败View等初始化
 * Created by user on 2016/11/16.
 */
public abstract class LoLBaseActivity extends AppCompatActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.btn_right)
    Button btnRight;

    private String title, btnRightStr;
    private int btnRightRes = -1, ivLeftRes = -1;

    protected abstract void initView(Bundle savedInstanceState);

    @OnClick(R.id.iv_left)
    protected void onClickImageViewLeft(){
        this.finish();
    }

    @OnClick(R.id.btn_right)
    protected void onClickBtnRight(){}

    protected void setRightButtonStr(String btnRightStr){
        this.btnRightStr = btnRightStr;
    }

    protected void setIvLeftRes(int ivLeftRes){
        this.ivLeftRes = ivLeftRes;
    }

    protected void setBtnRightRes(int ivLeftRes){
        this.ivLeftRes = ivLeftRes;
    }

    protected void setTitle(String title){
        this.title = title;
    }

    protected void setRightButtonVisibility(int visibility){
        if(btnRight != null){
            btnRight.setVisibility(visibility);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(this, R.color.system_bar_color);
        initView(savedInstanceState);
        initToolbar();
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup decorViewGroup = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(color);
            decorViewGroup.addView(statusBarView);
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Resources res = activity.getResources();
            int id = res.getIdentifier("status_bar_height", "dimen", "android");
            int height;
            try {
                height = res.getDimensionPixelSize(id);
            } catch (Resources.NotFoundException e) {
                height = Helper.dp2px(25);
            }
            return height;
        }
        return 0;
    }

    private void initToolbar() {
        ButterKnife.bind(this);
        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
        if(!TextUtils.isEmpty(btnRightStr)){
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setText(btnRightStr);
        } else {
            btnRight.setVisibility(View.GONE);
        }
        if(ivLeftRes != -1){
            ivLeft.setImageResource(ivLeftRes);
        }
        if(btnRightRes != -1){
            btnRight.setBackgroundResource(btnRightRes);
        }
    }

}
