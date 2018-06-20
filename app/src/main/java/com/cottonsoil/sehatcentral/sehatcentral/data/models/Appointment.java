package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Appointment {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("links")
    @Expose
    private List<AppointmentLinks> links = null;

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

    public List<AppointmentLinks> getLinks() {
        return links;
    }

    public void setLinks(List<AppointmentLinks> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", links=" + links +
                '}';
    }
}
