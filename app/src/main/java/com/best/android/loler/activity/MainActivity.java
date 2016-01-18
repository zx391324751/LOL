package com.best.android.loler.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.best.android.loler.R;
import com.best.android.loler.adapter.MainViewPagerAdapter;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.dao.AccountDao;
import com.best.android.loler.manager.PhotoManager;
import com.best.android.loler.model.Account;
import com.best.android.loler.util.FileUtil;

public class MainActivity extends FragmentActivity {

    private  DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private final int INDICATOR_COUNT = 3;
    private Account account;

    //帐号数据
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvServerName;
    private TextView tvLevel;
    private TextView tvFightLevel;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_main_iv_left_menu:
                    if(drawerLayout.isDrawerOpen(Gravity.LEFT))
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    else
                        drawerLayout.openDrawer(Gravity.LEFT);
                    break;
                case R.id.activity_main_btn_zhibo:
                    Intent intent = new Intent(MainActivity.this, DouyuActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private View.OnClickListener leftMenuItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.left_menu_layout_account_manager:
                    Intent intent = new Intent(MainActivity.this, AccountManagerActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileUtil.initAllFileDir();
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AccountDao accountDao = new AccountDao();
        account = accountDao.getSelectedAccount();
        initAccountData();
    }

    private void initView() {
        viewPager = (ViewPager)findViewById(R.id.activity_main_viewpager);
        drawerLayout = (DrawerLayout)findViewById(R.id.activity_main_drawerlayout);
        findViewById(R.id.activity_main_iv_left_menu).setOnClickListener(onClickListener);
        findViewById(R.id.left_menu_layout_account_manager).setOnClickListener(leftMenuItemOnClickListener);
        findViewById(R.id.activity_main_btn_zhibo).setOnClickListener(onClickListener);

        ivPhoto = (ImageView)findViewById(R.id.left_menu_iv_photo);
        tvServerName = (TextView) findViewById(R.id.left_menu_tv_server_name);
        tvName = (TextView) findViewById(R.id.left_menu_tv_name);
        tvLevel = (TextView) findViewById(R.id.left_menu_tv_level);
        tvFightLevel = (TextView) findViewById(R.id.left_menu_tv_fight_level);
    }

    private void initData(){
        MainViewPagerAdapter pagerAdapter = new MainViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    //初始化帐号数据，每次onresume调用该方法
    private void initAccountData(){
        if(account != null) {
            ivPhoto.setImageBitmap(PhotoManager.getInstance().getBitmapFromMemCache(NetConfig.getLolAccountPhotoUrl(account.photoId)));
            tvName.setText(account.accountName);
            tvServerName.setText(account.accountServerName);
            tvLevel.setText(account.tierDesc);
            tvFightLevel.setText("战斗力:" + account.fightLevel);
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            initTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initTab(int position) {
        LinearLayout layoutNews = (LinearLayout) findViewById(R.id.activity_main_layout_news);
        LinearLayout layoutHero = (LinearLayout) findViewById(R.id.activity_main_layout_hero);
        LinearLayout layoutPersonal = (LinearLayout) findViewById(R.id.activity_main_layout_personal);
        switch (position){
            case 0:
                layoutNews.setBackgroundColor(getResources().getColor(R.color.yellow));
                layoutHero.setBackgroundColor(getResources().getColor(R.color.white));
                layoutPersonal.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 1:
                layoutNews.setBackgroundColor(getResources().getColor(R.color.white));
                layoutHero.setBackgroundColor(getResources().getColor(R.color.yellow));
                layoutPersonal.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 2:
                layoutNews.setBackgroundColor(getResources().getColor(R.color.white));
                layoutHero.setBackgroundColor(getResources().getColor(R.color.white));
                layoutPersonal.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
        }
    }

}
