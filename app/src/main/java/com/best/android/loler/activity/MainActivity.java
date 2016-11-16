package com.best.android.loler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.best.android.loler.R;
import com.best.android.loler.adapter.MainViewPagerAdapter;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.dao.AccountDao;
import com.best.android.loler.http.LOLBoxApi;
import com.best.android.loler.manager.PhotoManager;
import com.best.android.loler.model.Account;
import com.best.android.loler.util.FileUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends LoLBaseActivity {

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

//        test();
        initView();
        initData();
    }

//    private void test(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://lolbox.duowan.com/")
//                .build();
//        LOLBoxApi.LOLServerListService service = retrofit.create(LOLBoxApi.LOLServerListService.class);
//        Call<ResponseBody> call = service.getLOLServerListService();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    Log.d("Test", response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }

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
//        findViewById(R.id.iv_left).setOnClickListener(onClickListener);
        findViewById(R.id.left_menu_layout_account_manager).setOnClickListener(leftMenuItemOnClickListener);
//        findViewById(R.id.activity_main_btn_zhibo).setOnClickListener(onClickListener);

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

    @Override
    protected void onClickImageViewLeft() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onClickBtnRight() {
        Intent intent = new Intent(MainActivity.this, DouyuActivity.class);
        startActivity(intent);
    }
}
