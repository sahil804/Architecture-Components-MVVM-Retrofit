package com.cottonsoil.sehatcentral.sehatcentral.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sahil on 6/27/2018.
 */
public class AppointmentList {

    @SerializedName("results")
    @Expose
    private List<Appointment> appointments = null;

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "AppointmentList{" +
                "appointments=" + appointments +
                '}';
    }
}