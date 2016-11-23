// Generated code from Butter Knife. Do not modify!
package com.best.android.loler.activity;

import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.internal.Utils;
import com.best.android.loler.R;
import java.lang.Override;

public class MainActivity_ViewBinding<T extends MainActivity> extends LoLBaseActivity_ViewBinding<T> {
  @UiThread
  public MainActivity_ViewBinding(T target, View source) {
    super(target, source);

    target.drawerLayout = Utils.findRequiredViewAsType(source, R.id.activity_main_drawerlayout, "field 'drawerLayout'", DrawerLayout.class);
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.activity_main_viewpager, "field 'viewPager'", ViewPager.class);
    target.ivPhoto = Utils.findRequiredViewAsType(source, R.id.left_menu_iv_photo, "field 'ivPhoto'", ImageView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.left_menu_tv_name, "field 'tvName'", TextView.class);
    target.tvServerName = Utils.findRequiredViewAsType(source, R.id.left_menu_tv_server_name, "field 'tvServerName'", TextView.class);
    target.tvLevel = Utils.findRequiredViewAsType(source, R.id.left_menu_tv_level, "field 'tvLevel'", TextView.class);
    target.tvFightLevel = Utils.findRequiredViewAsType(source, R.id.left_menu_tv_fight_level, "field 'tvFightLevel'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.drawerLayout = null;
    target.viewPager = null;
    target.ivPhoto = null;
    target.tvName = null;
    target.tvServerName = null;
    target.tvLevel = null;
    target.tvFightLevel = null;
  }
}
