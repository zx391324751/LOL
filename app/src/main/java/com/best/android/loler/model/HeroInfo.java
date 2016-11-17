package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BL06249 on 2015/11/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeroInfo implements Serializable{

    @JsonProperty("enName")
    public String enName;
    @JsonProperty("cnName")
    public String cnName;
    @JsonProperty("title")
    public String title;
    @JsonProperty("tags")
    public String tags;
    @JsonProperty("location")
    public String location;
    @JsonProperty("price")
    public String price;

    @JsonIgnore()
    public String photo;

}
