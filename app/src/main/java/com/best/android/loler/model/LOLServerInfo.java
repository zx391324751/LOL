package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by BL06249 on 2015/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLServerInfo {

    @JsonProperty("sn")
    public String sn;
    @JsonProperty("fn")
    public String fn;
    @JsonProperty("snEn")
    public String snEn;

}
