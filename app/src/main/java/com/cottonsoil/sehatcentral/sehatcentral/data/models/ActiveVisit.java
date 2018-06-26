package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sahil on 6/26/2018.
 */
public class ActiveVisit {

    @SerializedName("uuid")
    @Expose
    private String uuid;

    @SerializedName("display")
    @Expose
    private String display;

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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ActiveVisit{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", links=" + links +
                '}';
    }
}