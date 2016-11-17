package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by BL06249 on 2016/1/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DanmuServer {
    @JsonProperty("ip")
    public String ip;
    @JsonProperty("port")
    public int port;
}
