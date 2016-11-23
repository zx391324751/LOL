package com.best.android.loler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.best.android.loler.R;
import com.best.android.loler.adapter.MainViewPagerAdapter;
import com.best.android.loler.dao.AccountDao;
import com.best.android.loler.http.LOLBoxApi;
import com.best.android.loler.manager.PhotoManager;
import com.best.android.loler.model.Account;
import com.best.android.loler.util.FileUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LoLBaseActivity {

    @BindView(R.id.activity_main_drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_viewpager)
    ViewPager viewPager;
    private final int INDICATOR_COUNT = 3;
    private Account account;

    //帐号数据
    @BindView(R.id.left_menu_iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.left_menu_tv_name)
    TextView tvName;
    @BindView(R.id.left_menu_tv_server_name)
    TextView tvServerName;
    @BindView(R.id.left_menu_tv_level)
    TextView tvLevel;
    @BindView(R.id.left_menu_tv_fight_level)
    TextView tvFightLevel;

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
    protected void initView(Bundle savedInstanceState) {
        FileUtil.initAllFileDir();
        setContentView(R.layout.activity_main);
        setRightButtonStr("直播");
        setIvLeftRes(R.drawable.icon_menu);
        ButterKnife.bind(this);
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
        findViewById(R.id.left_menu_layout_account_manager).setOnClickListener(leftMenuItemOnClickListener);
    }

    private void initData(){
        MainViewPagerAdapter pagerAdapter = new MainViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void initAccountData(){
        if(account != null) {
            ivPhoto.setImageBitmap(PhotoManager.getInstance().getBitmapFromMemCache(LOLBoxApi.getLolAccountPhotoUrl(account.photoId)));
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

    @Override
    protected void onClickImageViewLeft() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onClickBtnRight() {
        Intent intent = new Intent(MainActivity.this, DouyuActivity.class);
        startActivity(intent);
    }
}
