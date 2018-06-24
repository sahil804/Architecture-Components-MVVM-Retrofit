package com.cottonsoil.sehatcentral.sehatcentral.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentDetailsEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;

import java.util.List;

@Dao
public interface AppointmentDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppointment(AppointmentDetailsEntity appointmentDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAppointmentDetails(List<AppointmentDetailsEntity> appointmentDetailsListList);

    @Query("SELECT * FROM appointmentDetails WHERE uuid = :uuid")
    AppointmentDetailsEntity getAppointmentByUuid(int uuid);

    @Query("SELECT * FROM appointmentDetails ORDER BY uuid DESC")
    LiveData<List<AppointmentDetailsEntity>> getAllAppointmentDetailsList();

    @Query("SELECT * FROM appointmentDetails ORDER BY uuid DESC")
    List<AppointmentDetailsEntity> getAllAppointmentDetailsListStatic();

    @Query("DELETE FROM appointmentDetails")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM appointmentDetails")
    int getCount();
}
