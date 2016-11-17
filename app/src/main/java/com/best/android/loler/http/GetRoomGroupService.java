package com.best.android.loler.http;

import android.content.Context;

import com.best.android.loler.config.NetConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by BL06249 on 2016/1/13.
 */
public class GetRoomGroupService extends BaseHttpService {

    private final int TIME_OUT = 60 * 1000;

    private Context context;
    private ResponseListener responseListener;

    public GetRoomGroupService(Context context){
        this.context = context;
    }

    public void send(ResponseListener listener) {
        this.responseListener = listener;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(TIME_OUT);
        RequestParams requestParams = new RequestParams();
        asyncHttpClient.get(context, NetConfig.DOUYU_ZHIBO_GROUP_URL, requestParams, responseHandler);
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
