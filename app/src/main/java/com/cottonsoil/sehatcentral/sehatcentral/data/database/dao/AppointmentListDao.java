package com.cottonsoil.sehatcentral.sehatcentral.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentList;

import java.util.List;

@Dao
public interface AppointmentListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppointment(Appointment appointment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAppointments(AppointmentList appointmentList);

    @Query("SELECT * FROM appointmentList WHERE uuid = :uuid")
    Appointment getAppointmentByUuid(String uuid);

    @Query("SELECT * FROM appointmentList ORDER BY uuid DESC")
    LiveData<AppointmentList> getAllAppointmentList();

    @Query("SELECT * FROM appointmentList ORDER BY uuid DESC")
    AppointmentList getAllAppointmentListStatic();

    @Query("DELETE FROM appointmentList")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM appointmentList")
    int getCount();
}
