package com.best.android.loler.httpService;

import android.content.Context;

import com.best.android.loler.config.NetConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by BL06249 on 2015/12/1.
 */
public class QueryVideoListService extends BaseHttpService{

    private final int TIME_OUT = 60*1000;
    private Context context;
    private ResponseListener responseListener;

    public QueryVideoListService(Context context){
        this.context = context;
    }

    public void send(ResponseListener listener, Object object) {
        int pageNum = (int)object;
        this.responseListener = listener;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(TIME_OUT);
        RequestParams requestParams = new RequestParams();
        asyncHttpClient.get(context, NetConfig.getVideoListUrl(null, pageNum), requestParams, responseHandler);
    }

    TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            responseListener.onFail(responseString);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            responseListener.onSuccess(responseString);
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
        }
    };
}
