// Generated code from Butter Knife. Do not modify!
package com.best.android.loler.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.best.android.loler.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoLBaseActivity_ViewBinding<T extends LoLBaseActivity> implements Unbinder {
  protected T target;

  private View view2131493092;

  private View view2131493094;

  @UiThread
  public LoLBaseActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'tvTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_left, "field 'ivLeft' and method 'onClickImageViewLeft'");
    target.ivLeft = Utils.castView(view, R.id.iv_left, "field 'ivLeft'", ImageView.class);
    view2131493092 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickImageViewLeft();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_right, "field 'btnRight' and method 'onClickBtnRight'");
    target.btnRight = Utils.castView(view, R.id.btn_right, "field 'btnRight'", Button.class);
    view2131493094 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickBtnRight();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.tvTitle = null;
    target.ivLeft = null;
    target.btnRight = null;

    view2131493092.setOnClickListener(null);
    view2131493092 = null;
    view2131493094.setOnClickListener(null);
    view2131493094 = null;

    this.target = null;
  }
}
