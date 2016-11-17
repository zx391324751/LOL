package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by BL06249 on 2016/1/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomInfo implements Serializable{
    @JsonProperty("room_id")
    public long roomId;
    @JsonProperty("room_src")
    public String roomSrc;
    @JsonProperty("cate_id")
    public int cateId;
    @JsonProperty("room_name")
    public String roomName;
    @JsonProperty("show_status")
    public int showStatus;
    @JsonProperty("show_time")
    public long showTime;
    @JsonProperty("owner_uid")
    public long ownerUid;
    @JsonProperty("specific_catalog")
    public String specificCatalog;
    @JsonProperty("specific_status")
    public int specificStatus;
    @JsonProperty("vod_quality")
    public int vodQuality;
    @JsonProperty("nick_name")
    public String nickName;
    @JsonProperty("online")
    public long online;
    @JsonProperty("url")
    public String url;
    @JsonProperty("game_url")
    public String gameUrl;
    @JsonProperty("game_name")
    public String gameName;
    @JsonProperty("child_id")
    public int childId;
    @JsonProperty("fans")
    public long fans;

}
