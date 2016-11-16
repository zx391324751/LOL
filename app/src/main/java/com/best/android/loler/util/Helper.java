package com.best.android.loler.util;

import com.best.android.loler.LOLApplication;

/**
 * Created by user on 2016/11/16.
 */

public class Helper {

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        float density = LOLApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    public static int px2dp(int px) {
        float density = LOLApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * sp转px
     *
     * @param sp
     * @return
     */
    public static int sp2px(float sp) {
        float scaledDensity = LOLApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scaledDensity + 0.5f);
    }

    /**
     * px转sp
     *
     * @param px
     * @return
     */
    public static int px2sp(float px) {
        float scaledDensity = LOLApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scaledDensity + 0.5f);
    }

}
