package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sahil on 6/26/2018.
 */
public class PrescriptionEncounter extends Encounter {

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

    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;


    /*@SerializedName("links")
    @Expose
    private List<Link> links = null;*/
    @SerializedName("resourceVersion")
    @Expose
    private String resourceVersion;

   /* public String getUuid() {
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    /*public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link>links) {
        this.links = links;
    }
*/
    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @Override
    public String toString() {
        return "PrescriptionEncounter{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", encounterDatetime='" + encounterDatetime + '\'' +
                ", orders=" + orders +
                ", links=" + links +
                ", resourceVersion='" + resourceVersion + '\'' +
                '}';
    }

    public class Order {

        @SerializedName("uuid")
        @Expose
        private String uuid;

        @SerializedName("display")
        @Expose
        private String display;

        @SerializedName("links")
        @Expose
        private List<Link> links = null;

        @SerializedName("type")
        @Expose
        private String type;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "uuid='" + uuid + '\'' +
                    ", display='" + display + '\'' +
                    ", links=" + links +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

}