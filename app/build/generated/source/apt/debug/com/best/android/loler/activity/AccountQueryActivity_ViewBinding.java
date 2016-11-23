// Generated code from Butter Knife. Do not modify!
package com.best.android.loler.activity;

import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.best.android.loler.R;
import java.lang.Override;

public class AccountQueryActivity_ViewBinding<T extends AccountQueryActivity> extends LoLBaseActivity_ViewBinding<T> {
  private View view2131492948;

  private View view2131492946;

  @UiThread
  public AccountQueryActivity_ViewBinding(final T target, View source) {
    super(target, source);

    View view;
    target.viewFlipper = Utils.findRequiredViewAsType(source, R.id.activity_account_query_vf, "field 'viewFlipper'", ViewFlipper.class);
    target.tvServer = Utils.findRequiredViewAsType(source, R.id.activity_account_query_tv_server_name, "field 'tvServer'", TextView.class);
    target.etName = Utils.findRequiredViewAsType(source, R.id.activity_account_query_et_player_name, "field 'etName'", EditText.class);
    target.webView = Utils.findRequiredViewAsType(source, R.id.activity_account_query_wb, "field 'webView'", WebView.class);
    view = Utils.findRequiredView(source, R.id.activity_account_query_btn_next, "method 'onNext'");
    view2131492948 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNext();
      }
    });
    view = Utils.findRequiredView(source, R.id.activity_account_query_layout_select_server, "method 'showServerListDialog'");
    view2131492946 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showServerListDialog();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.viewFlipper = null;
    target.tvServer = null;
    target.etName = null;
    target.webView = null;

    view2131492948.setOnClickListener(null);
    view2131492948 = null;
    view2131492946.setOnClickListener(null);
    view2131492946 = null;
  }
}
