package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by BL06249 on 2016/1/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupInfo implements Serializable{

    @JsonProperty("cate_id")
    public int roomId;
    @JsonProperty("game_icon")
    public String gameIcon;
    @JsonProperty("game_name")
    public String gameName;
    @JsonProperty("game_src")
    public String gameSrc;
    @JsonProperty("game_url")
    public String gameUrl;
    @JsonProperty("online_room")
    public int onlineRoom;
    @JsonProperty("short_name")
    public String shortName;

}
