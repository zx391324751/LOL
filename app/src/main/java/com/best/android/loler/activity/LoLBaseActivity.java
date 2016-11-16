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

/**
 * activity基类
 * 主要用于所有activity的共同元素的初始化，简化代码
 * 比如标题栏，状态栏，网络加载失败View等初始化
 * Created by user on 2016/11/16.
 */
public abstract class LoLBaseActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView ivLeft;
    private Button btnRight;

    private String title, btnRightStr;
    private int btnRightRes = -1;

    protected abstract void initView(Bundle savedInstanceState);

    protected void onClickImageViewLeft(){
        this.finish();
    }

    protected void onClickBtnRight(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBarColor(this, ContextCompat.getColor(this, R.color.system_bar_color));
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
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        btnRight = (Button)findViewById(R.id.btn_right);
        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
        if(!TextUtils.isEmpty(btnRightStr)){
            btnRight.setVisibility(View.VISIBLE);
//            if(btnRightRes != -1){
//                btnRight.setBackgroundResource(0);
//            }
        } else {
            btnRight.setVisibility(View.GONE);
        }

        ivLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_right:
                onClickBtnRight();
                break;
            case R.id.iv_left:
                onClickImageViewLeft();
                break;
        }
    }

}