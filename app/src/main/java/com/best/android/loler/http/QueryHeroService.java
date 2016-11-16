package com.best.android.loler.http;

import android.content.Context;

import com.best.android.loler.config.Constants;
import com.best.android.loler.config.NetConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;

/**
 * Created by BL06249 on 2015/11/26.
 */
public class QueryHeroService extends BaseHttpService {

    private final int TIME_OUT = 60 * 1000;

    private Context context;
    private ResponseListener responseListener;

    private int type;

    public QueryHeroService(Context context){
        this.context = context;
    }

    public void send(ResponseListener listener, Object object) {
        this.responseListener = listener;
        this.type = (int)object;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(TIME_OUT);
        RequestParams requestParams = new RequestParams();
        if(type == Constants.FREE_HERO)
            asyncHttpClient.get(context, NetConfig.FREE_HERO_URL, requestParams, responseHandler);
        else if(type == Constants.ALL_HERO)
            asyncHttpClient.get(context, NetConfig.ALL_HERO_URL, requestParams, responseHandler);
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
