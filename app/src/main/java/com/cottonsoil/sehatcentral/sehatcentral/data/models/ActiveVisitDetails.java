package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sahil on 6/26/2018.
 */
public class ActiveVisitDetails {

    @SerializedName("uuid")
    @Expose
    private String uuid;

    @SerializedName("display")
    @Expose
    private String display;

    @SerializedName("startDatetime")
    @Expose
    private String startDatetime;

    @SerializedName("stopDatetime")
    @Expose
    private Object stopDatetime;

    @SerializedName("encounters")
    @Expose
    private List<Encounter> encounters = null;

    @SerializedName("links")
    @Expose
    private List<Link> links = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }


    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Object getStopDatetime() {
        return stopDatetime;
    }

    public void setStopDatetime(Object stopDatetime) {
        this.stopDatetime = stopDatetime;
    }

    public List<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<Encounter> encounters) {
        this.encounters = encounters;
    }



    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ActiveVisitDetails{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", startDatetime='" + startDatetime + '\'' +
                ", stopDatetime=" + stopDatetime +
                ", encounters=" + encounters +
                ", links=" + links +
                '}';
    }
}