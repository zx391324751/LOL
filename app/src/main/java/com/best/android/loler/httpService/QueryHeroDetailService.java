package com.best.android.loler.httpService;

import android.content.Context;

import com.best.android.loler.config.NetConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by BL06249 on 2016/1/5.
 */
public class QueryHeroDetailService extends BaseHttpService {

    private final int TIME_OUT = 60 * 1000;
    private Context context;
    private ResponseListener listener;

    public QueryHeroDetailService(Context context){
        this.context = context;
    }

    public void send(ResponseListener listener, Object object) {
        this.listener = listener;
        String heroEnName = (String)object;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(TIME_OUT);
        RequestParams requestParams = new RequestParams();
        asyncHttpClient.post(context, NetConfig.getHeroDetailInfoUrl(heroEnName),requestParams, responseHandler);
    }

    TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            listener.onFail(responseString);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            listener.onSuccess(responseString);
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
        }
    };
}
