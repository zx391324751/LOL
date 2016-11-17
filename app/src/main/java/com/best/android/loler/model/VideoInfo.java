package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by BL06249 on 2015/12/1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInfo {

    @JsonProperty("vid")
    public String vid;
    @JsonProperty("udb")
    public String udb;
    @JsonProperty("letv_video_id")
    public String letvVideoId;
    @JsonProperty("cover_url")
    public String coverUrl;
    @JsonProperty("title")
    public String title;
    @JsonProperty("channelId")
    public String channelId;
    @JsonProperty("video_length")
    public long videoLength;
    @JsonProperty("upload_time")
    public String uploadTime;
    @JsonProperty("totalPage")
    public long totalPage;

}
