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
}
