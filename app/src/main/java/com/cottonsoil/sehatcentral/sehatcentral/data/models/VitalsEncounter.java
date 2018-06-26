package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sahil on 6/26/2018.
 */
public class VitalsEncounter extends Encounter{
    /*@SerializedName("uuid")
    @Expose
    private String uuid;

    @SerializedName("display")
    @Expose
    private String display;
*/
    @SerializedName("encounterDatetime")
    @Expose
    private String encounterDatetime;

    @SerializedName("obs")
    @Expose
    private List<Vital> vital = null;

    /*@SerializedName("links")
    @Expose
    private List<Link> links = null;
*/
    @SerializedName("resourceVersion")
    @Expose
    private String resourceVersion;

    /*public String getUuid() {
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
    }*/

    public String getEncounterDatetime() {
        return encounterDatetime;
    }

    public void setEncounterDatetime(String encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }

    public List<Vital> getVital() {
        return vital;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "VitalsEncounter{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", encounterDatetime='" + encounterDatetime + '\'' +
                ", vital=" + vital +
                ", links=" + links +
                ", resourceVersion='" + resourceVersion + '\'' +
                '}';
    }

    public class Vital {
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
            return "Vital{" +
                    "uuid='" + uuid + '\'' +
                    ", display='" + display + '\'' +
                    ", links=" + links +
                    '}';
        }
    }
}
