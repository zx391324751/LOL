package com.best.android.loler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * LOL账号
 * Created by BL06249 on 2015/12/16.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable{

    @DatabaseField
    @JsonIgnore()
    public boolean isSelect; //该账户是否被选择

    @DatabaseField(generatedId = true)
    @JsonIgnore()
    public Long id;

    @DatabaseField
    @JsonProperty("pn")
    public String accountName;

    @DatabaseField
    @JsonProperty("sn")
    public String accountServerId;

    @DatabaseField
    @JsonProperty("snFullName")
    public String accountServerName;

    @DatabaseField
    @JsonProperty("level")
    public String accountLevel;

    @DatabaseField
    @JsonProperty("zdl")
    public String fightLevel;

    @DatabaseField
    @JsonProperty("tierDesc")
    public String tierDesc;

    @DatabaseField
    @JsonProperty("icon")
    public int photoId;

}
