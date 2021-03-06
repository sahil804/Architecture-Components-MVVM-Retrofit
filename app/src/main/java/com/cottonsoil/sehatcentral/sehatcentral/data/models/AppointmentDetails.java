package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AppointmentDetails {

    @SerializedName("display")
    @Expose
    private String display;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @SerializedName("uuid")
    @Expose
    private String uuid;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Expose(serialize = false, deserialize = false)
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "AppointmentDetails{" +
                "display='" + display + '\'' +
                ", uuid='" + uuid + '\'' +
                ", timeSlot=" + timeSlot +
                ", paitient=" + paitient +
                '}';
    }

    @SerializedName("timeSlot")
    @Expose
    private TimeSlot timeSlot;

    public class TimeSlot {
        @SerializedName("startDate")
        @Expose
        private String startDate;
        @SerializedName("endDate")
        @Expose
        private String endDate;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        @Override
        public String toString() {
            return "TimeSlot{" +
                    "startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    '}';
        }
    }

    @SerializedName("patient")
    @Expose
    private Patient paitient;

    public Patient getPaitient() {
        return paitient;
    }

    public void setPaitient(Patient paitient) {
        this.paitient = paitient;
    }
}