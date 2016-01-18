package com.best.android.loler.config;

import java.net.URLEncoder;

/**
 * Created by BL06249 on 2015/11/23.
 */
public class NetConfig {

    //本周免费英雄
    public static final String FREE_HERO_URL = "http://lolbox.duowan.com/phone/apiHeroes.php?type=free&v=140&OSType=Android";
    //全部英雄
    public static final String ALL_HERO_URL = "http://lolbox.duowan.com/phone/apiHeroes.php?type=all&v=140&OSType=Android";
    //英雄视频
    public static final String HERO_VIDEO_URL = "http://box.dwstatic.com/apiVideoesNormalDuowan.php?";
    //英雄头像数据
    public static final String HERO_PHOTO_URL = "http://img.lolbox.duowan.com/champions/";
    //获取视频播放地址url
    public static final String QUERY_VIDEO_ADDRESS_URL = "http://box.dwstatic.com/apiVideoesNormalDuowan.php?action=f&cf=android&check_code=null&format=json&payer_name=null&plat=android2.2&uu=&ver=2.0&vu=null&sign=signxxxxx";

    //查战绩
    public static final String QUERY_RECORD_URL = "http://lolbox.duowan.com/phone/playerSearchNew.php?lolboxAction=toInternalWebView";
    //获取LOL服务器列表
    public static final String GET_LOL_SERVER_LIST_URL = "http://lolbox.duowan.com/phone/apiServers.php";
    //获取玩家信息
    public static final String LOL_USER_INFO_URL = "http://lolbox.duowan.com/phone/apiCheckUser.php?action=getPlayersInfo";
    //玩家信息WebView 地址
    public static final String LOL_USER_INFO_WEB_URL = "http://zdl.mbox.duowan.com/phone/playerDetailNew.php?lolboxAction=toPlayerDetail";
    //玩家头像数据
    public static final String LOL_ACCOUNT_PHOTO_URL = "http://img.lolbox.duowan.com/profileIcon/profileIcon";
    //英雄技能图标
    public static final String LOL_HERO_SKILL_PHOTO_URL = "http://img.lolbox.duowan.com/abilities/";
    //英雄详细信息
    public static final String LOL_HERO_DETAIL_INFO_URL = "http://lolbox.duowan.com/phone/apiHeroDetail.php?OSType=iOS9.1&v=140&heroName=";
    //英雄出装
    public static final String LOL_HERO_CZ_URL = "http://db.duowan.com/lolcz/img/ku11/api/lolcz.php?limit=7&championName=";
    //装备图片
    public static final String LOL_ZB_PHOTO_URL = "http://img.lolbox.duowan.com/zb/";


    public static String getHeroPhotoUrl(String heroName){
        return HERO_PHOTO_URL+ heroName + "_120x120.jpg";
    }

    public static String getVideoListUrl(String heroName, int pageNum){
        String url = HERO_VIDEO_URL + "action=l&v=140&OSType=iOS9.1&src=letv";
        if(heroName != null)
            url = url + "&tag=" + heroName;
        if(pageNum > 0)
            url = url + "&p=" + pageNum;
        return url;
    }

    public static String getVideoAddressUrl(String vid){
        return QUERY_VIDEO_ADDRESS_URL + "&vid=" + vid;
    }

    public static String getLoLUserInfoUrl(String serverName, String userName){
        return LOL_USER_INFO_URL + "&serverName=" + serverName + "&target=" + userName;
    }

    public static String getLolUserInfoWebUrl(String serverName, String userName){
        return LOL_USER_INFO_WEB_URL + "&sn=" + serverName + "&pn=" + userName;
    }

    public static String getLolAccountPhotoUrl(int photoID){
        return LOL_ACCOUNT_PHOTO_URL + photoID + ".jpg";
    }

    public static String getSkillPhotoUrl(String skill){
        return LOL_HERO_SKILL_PHOTO_URL + skill + "_64x64.png?v=10&OSType=iOS7.0.3";
    }

    public static String getHeroDetailInfoUrl(String heroName){
        return LOL_HERO_DETAIL_INFO_URL + heroName;
    }

    public static String getLolHeroCzUrl(String heroName){
        return LOL_HERO_CZ_URL + heroName;
    }

    public static String getLolZbPhotoUrl(String id){
        return LOL_ZB_PHOTO_URL + id + "_64x64.png";
    }


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
