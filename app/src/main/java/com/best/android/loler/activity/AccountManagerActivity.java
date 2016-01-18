package com.best.android.loler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.best.android.loler.R;
import com.best.android.loler.adapter.ListviewAccountAdapter;
import com.best.android.loler.adapter.OnItemClickListener;
import com.best.android.loler.dao.AccountDao;
import com.best.android.loler.httpService.BaseHttpService;
import com.best.android.loler.httpService.QueryAccountService;
import com.best.android.loler.model.Account;
import com.best.android.loler.view.ScrollerLinearLayout;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BL06249 on 2015/12/16.
 */
public class AccountManagerActivity extends AppCompatActivity {

    private static final int ADD_ACCOUNT_CODE = 1001;

    private ListView lvAccount;
    private List<Account> listAccount;
    private ListviewAccountAdapter accountAdapter;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_account_manager_iv_back:
                    AccountManagerActivity.this.finish();
                    break;
                case R.id.activity_account_manager_btn_add:
                    Intent intent = new Intent(AccountManagerActivity.this, AccountQueryActivity.class);
                    startActivityForResult(intent, ADD_ACCOUNT_CODE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);

        initView();
        initData();
    }

    private void initView() {

        findViewById(R.id.activity_account_manager_iv_back).setOnClickListener(onClickListener);
        findViewById(R.id.activity_account_manager_btn_add).setOnClickListener(onClickListener);

        lvAccount = (ListView)findViewById(R.id.activity_account_manager_lv_account);
        lvAccount.setOnItemClickListener(new OnAccountListViewItemClickListener());
    }

    private void initData(){
        listAccount = new ArrayList<Account>();
        AccountDao accountDao = new AccountDao();
        listAccount = accountDao.queryAllRecord();
        accountAdapter = new ListviewAccountAdapter(this, listAccount);
        lvAccount.setAdapter(accountAdapter);

        updataAccount();
    }

    //更新每个帐号的信息
    private void updataAccount() {
        for(int i=0; i<listAccount.size(); i++){
            QueryAccountService queryAccountService = new QueryAccountService(this);
            queryAccountService.send(queryAccountListener, listAccount.get(i).accountServerId, listAccount.get(i).accountName);
        }
    }

    BaseHttpService.ResponseListener queryAccountListener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                for(int i=0; i<listAccount.size(); i++){
                    if(jsonObject.has(listAccount.get(i).accountName)){
                        String accountInfo = jsonObject.getString(listAccount.get(i).accountName);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Account account = objectMapper.readValue(accountInfo, Account.class);
                        //当匹配到用户名和大区名的时候  更新数据库
                        if(account.accountServerId.equals(listAccount.get(i).accountServerId)
                                && account.accountName.equals(listAccount.get(i).accountName)){
                            listAccount.get(i).accountLevel = account.accountLevel;
                            listAccount.get(i).fightLevel = account.fightLevel;
                            listAccount.get(i).tierDesc = account.tierDesc;
                            listAccount.get(i).photoId = account.photoId;
                            AccountDao accountDao = new AccountDao();
                            accountDao.updateAccount(listAccount.get(i));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFail(String errorMsg) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            if (requestCode == ADD_ACCOUNT_CODE){
                Account account = (Account)data.getSerializableExtra("ACCOUNT");
                //如果只有一个账号，那么默认选择；否则不选择
                if(listAccount.size() <= 0){
                    account.isSelect = true;
                } else {
                    account.isSelect = false;
                }
                //判断是否该账号已经存在
                boolean isExist = false;
                for(int i=0;i < listAccount.size(); i++){
                    isExist = (listAccount.get(i).accountName.equals(account.accountName)
                            && listAccount.get(i).accountServerId.equals(account.accountServerId));
                    if(isExist) {
                        Toast.makeText(this, "该账号已经存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //存到数据库里面
                AccountDao accountDao = new AccountDao();
                accountDao.add(account);
                //更新listview
                listAccount.add(account);
                accountAdapter.notifyDataSetChanged();
            }
        }
    }

    class OnAccountListViewItemClickListener implements AdapterView.OnItemClickListener {

        //滑动标志：[0, (all position -1)] 表示某个位置处于左滑状态， -1表示没有任何位置处于左滑状态
        private int scrollerFlag;

        public OnAccountListViewItemClickListener(){
            scrollerFlag = -1;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ScrollerLinearLayout scrollerLayout = (ScrollerLinearLayout)lvAccount.getChildAt(position);
            if(scrollerFlag == -1){
                scrollerLayout.smoothOpenMenu();
                scrollerFlag = position;
            } else {
                if(position == scrollerFlag){
                    scrollerLayout.smoothCloseMenu();
                    scrollerFlag = -1;
                } else {
                    ScrollerLinearLayout closeLayout = (ScrollerLinearLayout)lvAccount.getChildAt(scrollerFlag);
                    closeLayout.smoothCloseMenu();
                    scrollerLayout.smoothOpenMenu();
                    scrollerFlag = position;
                }
            }
        }
    }

}
