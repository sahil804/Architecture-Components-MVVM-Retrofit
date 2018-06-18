package com.cottonsoil.sehatcentral.sehatcentral.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cottonsoil.sehatcentral.sehatcentral.models.Appointment;

import java.util.List;

@Dao
public interface AppointmentListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppointment(Appointment appointment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAppointments(List<Appointment> appointmentList);

    @Query("SELECT * FROM appointmentList WHERE uuid = :uuid")
    Appointment getAppointmentByUuid(int uuid);

    @Query("SELECT * FROM appointmentList ORDER BY uuid DESC")
    LiveData<List<Appointment>> getAllAppointmentList();

    @Query("DELETE FROM appointmentList")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM appointmentList")
    int getCount();
}
