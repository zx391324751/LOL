package com.douyu.lib.xdanmuku.x;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by BL06249 on 2016/1/15.
 */
public class DanmuClient {

    private static DanmuClient client;
    private Handler handler;
    private JniDanmu jniDanmu;
    private DanmuListener listener = null;

    private DanmuClient(Context context){
        HandlerThread localHandlerThread = new HandlerThread("DanmuClient");
        localHandlerThread.start();
        this.handler = new Handler(localHandlerThread.getLooper());
        this.jniDanmu = new JniDanmu(context);
        this.jniDanmu.setDanmuInfoListener(new MyDanmuListener());
    }

    public static DanmuClient init(Context paramContext) {
        if (client == null)
            client = new DanmuClient(paramContext);
        return client;
    }

    public void connect(final Context context, final String ip, final int port){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                if (client != null) {
                    int result = jniDanmu.startDanmu(context, ip, port);
                    System.out.println("返回码 = " + result);
                }
            }
        });
    }

    public void reConnect(String ip, int port){
        int result = jniDanmu.reStartDanmu(ip, port);
    }

    class MyDanmuListener implements JniDanmu.DanmuInfoListener {
        MyDanmuListener() {
        }

        public void onCallBack(int paramInt, String paramString) {
            System.out.println("字幕:" + paramString);
        }
    }

}
