package com.best.android.loler.config;

import java.net.URLEncoder;

/**
 * Created by BL06249 on 2015/11/23.
 */
public class NetConfig {

    public static final String LOLBOX_BASE_URL_1 = "http://lolbox.duowan.com/";
    public static final String LOLBOX_BASE_URL_2 = "http://box.dwstatic.com/";
    public static final String LOLBOX_BASE_URL_3 = "http://db.duowan.com/";

    public static final String DOUYU_BASE_URL_1 = "http://www.douyutv.com/";

    /************   斗鱼tv接口    **************/
    //直播类型分组接口
    public static final String DOUYU_ZHIBO_GROUP_URL = "http://www.douyutv.com/api/v1/game?aid=android&client_sys=android";
    public static String getRoomListUrl(int roomid, int offset){
        return "http://www.douyutv.com/api/v1/live/"
                + roomid
                + "?aid=android&client_sys=android&limit=20&offset="
                + offset
                +"&time=1452503460&auth=8124d149ce3c20c3e4808a0603d3e1a0";
    }
    public static String getRoomDetailInfoUrl(String url){

        return "http://www.douyutv.com/api/v1/" + url;
    }
}
