package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 推荐出装信息
 * Created by BL06249 on 2016/1/7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLczInfo {
    @JsonProperty("record_id")
    public String recordId;
    @JsonProperty("title")
    public String title;
    @JsonProperty("author")
    public String author;
    @JsonProperty("skill")
    public String skill;
    @JsonProperty("pre_cz")
    public String preCZ;
    @JsonProperty("pre_explain")
    public String preExplain;
    @JsonProperty("mid_cz")
    public String midCZ;
    @JsonProperty("mid_explain")
    public String midExplain;
    @JsonProperty("end_cz")
    public String endCZ;
    @JsonProperty("end_explain")
    public String endExplain;
    @JsonProperty("nf_cz")
    public String nfCZ;
    @JsonProperty("nf_explain")
    public String nfExplain;
    @JsonProperty("time")
    public String time;
    @JsonProperty("server")
    public String lolServer;

}
