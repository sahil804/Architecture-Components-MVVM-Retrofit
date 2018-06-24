package com.cottonsoil.sehatcentral.sehatcentral.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "appointmentDetails")
public class AppointmentDetailsEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "startDate")
    private String startDate;

    @ColumnInfo(name = "endDate")
    private String endDate;

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "AppointmentDetailsEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
