package com.cottonsoil.sehatcentral.sehatcentral.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface AppointmentListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppointment(AppointmentEntity appointment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAppointments(List<AppointmentEntity> appointmentList);

    @Query("SELECT * FROM appointmentList WHERE uuid = :uuid")
    AppointmentEntity getAppointmentByUuid(String uuid);

    @Query("SELECT * FROM appointmentList ORDER BY uuid DESC")
    LiveData<List<AppointmentEntity>> getAllAppointmentList();

    @Query("SELECT * FROM appointmentList ORDER BY uuid DESC")
    List<AppointmentEntity> getAllAppointmentListStatic();

    @Query("DELETE FROM appointmentList")
    int deleteAll();

    @Query("DELETE FROM appointmentList WHERE date =:date")
    int deleteByDate(Date date);

    @Query("SELECT COUNT(*) FROM appointmentList")
    int getCount();
}