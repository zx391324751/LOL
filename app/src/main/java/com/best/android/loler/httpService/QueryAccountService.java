package com.best.android.loler.httpService;

import android.app.ProgressDialog;
import android.content.Context;

import com.best.android.loler.config.NetConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by BL06249 on 2015/12/17.
 */
public class QueryAccountService{

    private final int TIME_OUT = 60 * 1000;

    private Context context;
    private BaseHttpService.ResponseListener responseListener;
    private ProgressDialog progressDialog;

    public QueryAccountService(Context context){
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("正在查询战绩...");
    }

    public void send(BaseHttpService.ResponseListener listener, String server, String name) {
        this.responseListener = listener;
        progressDialog.show();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(TIME_OUT);
        RequestParams requestParams = new RequestParams();
        asyncHttpClient.get(context, NetConfig.getLoLUserInfoUrl(server, name), requestParams, responseHandler);
    }

    TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            responseListener.onFail(responseString);
            progressDialog.dismiss();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            responseListener.onSuccess(responseString);
            progressDialog.dismiss();
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
        }
    };

}
