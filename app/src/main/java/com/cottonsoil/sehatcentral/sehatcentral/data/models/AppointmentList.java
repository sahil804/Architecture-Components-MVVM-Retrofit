package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentList {

    @SerializedName("results")
    @Expose
    private List<Appointment> results = null;

    public List<Appointment> getAppointments() {
        return results;
    }

    public void setAppointments(List<Appointment> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "AppointmentList{" +
                "results=" + results +
                '}';
    }
}
