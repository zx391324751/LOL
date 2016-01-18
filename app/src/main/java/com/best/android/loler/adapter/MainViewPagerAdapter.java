package com.best.android.loler.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.best.android.loler.R;
import com.best.android.loler.fragment.HeroFragment;
import com.best.android.loler.fragment.NewsFragment;
import com.best.android.loler.fragment.PersonFragment;
import com.best.android.loler.model.TabInfo;

/**
 * Created by BL06249 on 2015/11/23.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter{

    private Context context;
    private TabInfo tabInfo[] = {new TabInfo("新闻资料", -1), new TabInfo("英雄资料", -1), new TabInfo("个人主页", -1)};

    public MainViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabInfo[position].tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return NewsFragment.newInstance(position);
            case 1:
                return HeroFragment.newInstance(position);
            default:
                return PersonFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return tabInfo.length;
    }

}
