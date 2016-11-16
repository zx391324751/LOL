package com.best.android.loler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.best.android.loler.R;
import com.best.android.loler.adapter.LOLServerAdapter;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.http.BaseHttpService;
import com.best.android.loler.http.GetLOLServerListService;
import com.best.android.loler.http.QueryAccountService;
import com.best.android.loler.model.Account;
import com.best.android.loler.model.LOLServerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by BL06249 on 2015/12/16.
 */
public class AccountQueryActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private Button btnConfirm;

    private TextView tvServer;
    private EditText etName;
    private Account account;
    private WebView webView;

//    private ListView lvServer;
    private LOLServerInfo listServer[];
    private int selectPosition;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_account_query_btn_next:
                    onNext();
                    break;
                case R.id.activity_account_query_btn_confirm:
                    onConfirm();
                    break;
                case R.id.activity_account_query_iv_back:
                    AccountQueryActivity.this.finish();
                    break;
                case R.id.activity_account_query_layout_select_server:
                    showServerListDialog();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_query);

        selectPosition = 0;
        GetLOLServerListService getLOLServerListService = new GetLOLServerListService(this);
        getLOLServerListService.send(getLOLServerListener, null);

        initView();
    }

    private void initView() {
        findViewById(R.id.activity_account_query_btn_next).setOnClickListener(onClickListener);
        findViewById(R.id.activity_account_query_iv_back).setOnClickListener(onClickListener);
        findViewById(R.id.activity_account_query_layout_select_server).setOnClickListener(onClickListener);

        btnConfirm = (Button)findViewById(R.id.activity_account_query_btn_confirm);
        btnConfirm.setOnClickListener(onClickListener);
        btnConfirm.setVisibility(View.INVISIBLE);
        viewFlipper = (ViewFlipper)findViewById(R.id.activity_account_query_vf);

        tvServer = (TextView)findViewById(R.id.activity_account_query_tv_server_name);
        etName = (EditText)findViewById(R.id.activity_account_query_et_player_name);
        webView = (WebView)findViewById(R.id.activity_account_query_wb);
    }

    //点击下一步
    private void onNext() {
        String name = etName.getText().toString().trim();
        String server = tvServer.getText().toString().trim();
        if(name.length() <= 0 || server.length() <= 0){
            Toast.makeText(this, "请输入名称并且选择该名称所在的服务器", Toast.LENGTH_SHORT).show();
            return;
        }

        QueryAccountService queryAccountService = new QueryAccountService(this);
        queryAccountService.send(queryAccountListener, listServer[selectPosition].sn, name);

    }

    //点击确认
    private void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("ACCOUNT", account);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    //  点击弹出LOL服务器列表选择框
    private void showServerListDialog() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;    //得到宽度
        int height = dm.heightPixels;  //得到高度

        if(listServer != null && listServer.length > 0) {

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = width * 7/8;
            layoutParams.height = height * 3/6;
            View view = LayoutInflater.from(AccountQueryActivity.this).inflate(R.layout.dialog_select_server, null);
            final AppCompatDialog serverListDialog = new AppCompatDialog(AccountQueryActivity.this);
            serverListDialog.setContentView(view, layoutParams);

            ListView lvServer = (ListView)serverListDialog.findViewById(R.id.dialog_select_server_lv);
            lvServer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectPosition = position;
                    serverListDialog.dismiss();
                    tvServer.setText(listServer[selectPosition].fn);
                }
            });
            LOLServerAdapter lolServerAdapter = new LOLServerAdapter(AccountQueryActivity.this, listServer, selectPosition);
            if(lvServer != null)
                lvServer.setAdapter(lolServerAdapter);
            serverListDialog.show();
        }
    }

    private BaseHttpService.ResponseListener getLOLServerListener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            initServerJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            Toast.makeText(AccountQueryActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
        }
    };

    private void initServerJson(String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            listServer = objectMapper.readValue(result, LOLServerInfo[].class);
            if(listServer != null && listServer.length > 0) {
                tvServer.setText(listServer[0].fn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BaseHttpService.ResponseListener queryAccountListener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            initAccountJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            Toast.makeText(AccountQueryActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
        }
    };

    private void initAccountJson(String result) {
        String userName = etName.getText().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(!jsonObject.has(userName)){
                Toast.makeText(this, "账号不存在", Toast.LENGTH_SHORT).show();
                return;
            }
            result = jsonObject.getString(userName);
            Account accountSearch = objectMapper.readValue(result, Account.class);
            this.account = accountSearch;
            webView.loadUrl(NetConfig.getLolUserInfoWebUrl(account.accountServerId, account.accountName));
            viewFlipper.showNext();
            btnConfirm.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
