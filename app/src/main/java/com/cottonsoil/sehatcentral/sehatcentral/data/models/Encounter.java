package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sahil on 6/26/2018.
 */
public class Encounter {

    @SerializedName("uuid")
    @Expose
    protected String uuid;

    @SerializedName("display")
    @Expose
    protected String display;

    @SerializedName("links")
    @Expose
    protected List<Link> links = null;

    @Expose(deserialize = false)
    private List<VitalsEncounter> vitalsEncounters;

    @Expose(deserialize = false)
    private List<PrescriptionEncounter> prescriptionEncounters;

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
        return "Encounter{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", links=" + links +
                ", vitalsEncounters=" + vitalsEncounters +
                ", prescriptionEncounters=" + prescriptionEncounters +
                '}';
    }
}