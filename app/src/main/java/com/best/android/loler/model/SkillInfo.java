package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by BL06249 on 2016/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillInfo {

    @JsonProperty("name")
    public String skillName; //技能名字
    @JsonProperty("cooldown")
    public String coolDown; //冷却时间
    @JsonProperty("description")
    public String description; //技能描述
    @JsonProperty("effect")
    public String effect; //影响
    @JsonProperty("cost")
    public String cost; //消耗
    @JsonProperty("range")
    public String range; //范围
}
