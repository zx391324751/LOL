package com.best.android.loler.http;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 2016/11/18.
 */

public class DouyuApi {

    public interface ZhiboService{
        @GET("api/v1/live/{roomid}")
        public String getRoomList(@Query("aid")String aid, @Query("client_sys") String sys
                , @Query("limit")int limit, @Query("offset") int offset
                , @Query("time") String time, @Query("auth")String auth
                , @Query("roomid")int roomId);
        @GET("api/v1/game")
        public String getRoomGroup();
    }

    public static String getRoomDetailInfoUrl(String url){
        return "http://www.douyutv.com/api/v1/" + url;
    }

}
