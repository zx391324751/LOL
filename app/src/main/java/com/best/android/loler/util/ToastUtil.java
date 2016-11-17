package com.best.android.loler.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by BL06249 on 2015/11/30.
 */
public class ToastUtil {

    public static void showShortMsg(Context context, String msg){
        if(context != null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

//    public static void showLongMsg(Context context, String msg){
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//    }

}
