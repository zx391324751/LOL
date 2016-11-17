package com.douyu.lib.xdanmuku.x;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by BL06249 on 2016/1/13.
 */
public class JniDanmu {

    private static final String TAG = "ZC_JAVA_N_JniLive";
    private long mNativeContext;

    public static abstract interface DanmuInfoListener {
        public abstract void onCallBack(int paramInt, String paramString);
    }

    private static boolean isInitSuccessed = false;
    private Context context;
    private DanmuInfoListener danmuInfoListener;

    public JniDanmu(Context context) {
        this.context = context;
        if(!isInitSuccessed)
            init();
        c();
    }

    static void init() {
        if(isInitSuccessed)
            return;
        try {
            System.loadLibrary("danmu");
            Log.i("ss", "System.loadLibrary成功");
            isInitSuccessed = true;
            return;
        }
        catch (Exception localException) {
            while (true)
                Log.e("ss", "System.loadLibrary失败");
        }
    }

    private class EventHandler extends Handler
    {
        private JniDanmu b;

        public EventHandler(JniDanmu paramLooper, Looper arg3)
        {
            super();
            this.b = paramLooper;
        }

        public void handleMessage(Message paramMessage)
        {
            Log.d("id = " + paramMessage.what, (String)paramMessage.obj);
        }
    }

    private EventHandler g;

    void c() {
        HandlerThread localHandlerThread = new HandlerThread("RecordEngine");
        localHandlerThread.start();
        try {
            //// TODO: 2016/1/18 这里是伪装context对象，包名是斗鱼的包名，但是set_up还是不成功，没有提示，不知道具体原因. :(
            Context douyuContext = context.createPackageContext("air.tv.douyu.android", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            this.g = new EventHandler(this, localHandlerThread.getLooper());
            System.out.println("setUp");
            //// TODO: 2016/1/18 前面这个参数传入的作用应该是提供jni方法回调入口，这个猜测正确性有待考证?
            int result = native_setup(new WeakReference(this), douyuContext);
            System.out.println("result = " + result);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void setDanmuInfoListener(DanmuInfoListener paramDanmuInfoListener) {
        this.danmuInfoListener = paramDanmuInfoListener;
    }

    private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2) {
        if (paramObject1 == null)
            return;
        JniDanmu localJniDanmu = (JniDanmu)((WeakReference)paramObject1).get();
        if (localJniDanmu == null) {
            Log.e("ZC_JAVA_N_JniLive", "[postEventFromNative] RecordEngine is null");
            return;
        }
        if (localJniDanmu.g != null) {
            Message localMessage = localJniDanmu.g.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
            localJniDanmu.g.sendMessage(localMessage);
            System.out.println("信息" + localMessage.toString());
            return;
        }
        Log.e("ZC_JAVA_N_JniLive", "[postEventFromNative] mEventHandler is null");
    }


    private native String native_authcodeDecode(Context paramContext, String paramString1, String paramString2);

    private native String native_authcodeEncode(Context paramContext, String paramString1, String paramString2);

    private native String native_getCodeE(Context paramContext);

    private native String native_getSDKE(Context paramContext);

    private native int native_gift(int paramInt);

    private native String native_makeUrl(Context paramContext, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4, int paramInt);

    private native int native_ranklist(String paramString);

    private native int native_release();

    private native int native_relogin(String paramString, int paramInt);

    private native int native_sendMsg(String paramString);

    private native int native_sendPlayPoint(String[] paramArrayOfString);

    private native int native_sendYuWan(String paramString);

    private native int native_setParameters(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);

    private native int native_setup(Object paramObject, Context paramContext);

    private native int native_start(Context paramContext, String paramString, int paramInt);

    private native int native_stop();

    private native int native_tasktime(String paramString);

    public String authcodeEncode(Context paramContext, String paramString1, String paramString2){
        return native_authcodeEncode(paramContext, paramString1, paramString2);
    }

    public String makeUrl(Context paramContext, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4, int paramInt){

        return native_makeUrl(paramContext, paramString, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramArrayOfString4, paramInt);
    }

    //// TODO: 2016/1/18  native_start，90%可能性是链接弹幕服务器的，可是一直连不上，猜测是setup没成功的原因。(做下笔记，捋下思路)
    public int startDanmu(Context context, String ip, int port){
        System.out.println("context = " + this.context.toString() + "  ip"+ ip + " port = " + port);
        return native_start(this.context, ip, port);
    }

    public int reStartDanmu(String ip, int port){
        return native_relogin(ip, port);
    }

}
