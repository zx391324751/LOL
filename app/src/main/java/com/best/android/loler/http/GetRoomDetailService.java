package com.best.android.loler.http;

import android.content.Context;

import com.best.android.loler.config.NetConfig;
import com.douyu.lib.xdanmuku.x.JniDanmu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.net.URLEncoder;

/**
 * Created by BL06249 on 2016/1/13.
 */
public class GetRoomDetailService extends BaseHttpService {

    private final int TIME_OUT = 60 * 1000;

    private Context context;
    private ResponseListener responseListener;

    public GetRoomDetailService(Context context){
        this.context = context;
    }

    public void send(ResponseListener listener, long roomId) {
        this.responseListener = listener;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(TIME_OUT);
        RequestParams requestParams = new RequestParams();
        JniDanmu jniDanmu = new JniDanmu(context.getApplicationContext());
        String time = String.valueOf(60L * (System.currentTimeMillis() / 1000L / 60L));
        String roomStr = "room/"+ roomId +"?";
        String str1[] = new String[]{"aid", "client_sys", "time"};
        String str2[] = new String[]{URLEncoder.encode("android"), URLEncoder.encode("android"), URLEncoder.encode(time)};
        String str3[] = new String[]{};
        String str4[] = new String[]{};
        String result = jniDanmu.makeUrl(context.getApplicationContext(), roomStr, str1, str2, str3, str4, 1);

        asyncHttpClient.get(context, NetConfig.getRoomDetailInfoUrl(result), requestParams, responseHandler);
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
