// Generated code from Butter Knife. Do not modify!
package com.best.android.loler.activity;

import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.internal.Utils;
import com.best.android.loler.R;
import java.lang.Override;

public class AccountManagerActivity_ViewBinding<T extends AccountManagerActivity> extends LoLBaseActivity_ViewBinding<T> {
  @UiThread
  public AccountManagerActivity_ViewBinding(T target, View source) {
    super(target, source);

    target.lvAccount = Utils.findRequiredViewAsType(source, R.id.activity_account_manager_lv_account, "field 'lvAccount'", ListView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.lvAccount = null;
  }
}
