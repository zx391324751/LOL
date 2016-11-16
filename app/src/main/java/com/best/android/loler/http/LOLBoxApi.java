package com.best.android.loler.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * LOL盒子相关的接口
 * Created by user on 2016/11/16.
 */

public class LOLBoxApi{

    //英雄头像数据
    public static final String HERO_PHOTO_URL = "http://img.lolbox.duowan.com/champions/";
    //玩家信息WebView 地址
    public static final String LOL_USER_INFO_WEB_URL = "http://zdl.mbox.duowan.com/phone/" +
            "playerDetailNew.php?lolboxAction=toPlayerDetail";
    //玩家头像数据
    public static final String LOL_ACCOUNT_PHOTO_URL = "http://img.lolbox.duowan.com/profileIcon/profileIcon";
    //英雄技能图标
    public static final String LOL_HERO_SKILL_PHOTO_URL = "http://img.lolbox.duowan.com/abilities/";

    public interface LOLServerListService{
        @GET("phone/apiServers.php")
        Call<ResponseBody> getLOLServerListService();
    }

    public interface LOLHeroService{
        //type = free:周免英雄  type = all:全部英雄
        @GET("phone/apiHeroes.php")
        Call<ResponseBody> getHeroList(@Query("type")String type, @Query("v")int v, @Query("OSType")String os);

        @GET("phone/apiHeroDetail")
        Call<ResponseBody>getHeroDetailInfo(@Query("heroName")String name);

        @GET("lolcz/img/ku11/api/lolcz.php")
        Call<ResponseBody>getHeroCZInfo(@Query("championName")String name);
    }

    public interface LOLVideoService{
        @GET("apiVideoesNormalDuowan.php")
        Call<ResponseBody>getVideoList(@Query("tag")String heroName, @Query("p") int pageNum);

        @GET("apiVideoesNormalDuowan.php")
        Call<ResponseBody>getVideoInfo(@Query("vid")String vid);
    }

    public interface LOLPlayerInfoService{
        @GET("phone/apiCheckUser.php")
        Call<ResponseBody>getPlayerInfo(@Query("serverName") String serverName, @Query("target") String userName);
    }

    /**图片和网页相关的url**/
    public static String getLolUserInfoWebUrl(String serverName, String userName){
        return LOL_USER_INFO_WEB_URL + "&sn=" + serverName + "&pn=" + userName;
    }
    public static String getHeroPhotoUrl(String heroName){
        return HERO_PHOTO_URL+ heroName + "_120x120.jpg";
    }
    public static String getLolAccountPhotoUrl(int photoID){
        return LOL_ACCOUNT_PHOTO_URL + photoID + ".jpg";
    }
    public static String getSkillPhotoUrl(String skill){
        return LOL_HERO_SKILL_PHOTO_URL + skill + "_64x64.png?v=10&OSType=iOS7.0.3";
    }

}
