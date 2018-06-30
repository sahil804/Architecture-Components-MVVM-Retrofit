package com.cottonsoil.sehatcentral.sehatcentral.data.mappers;

import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.Utility;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppointmentMapper {

    public static Appointment mapEntityToModel(AppointmentEntity appointmentEntity) {
        if (appointmentEntity == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final Appointment appointment = new Appointment();
        appointment.setDisplay(appointmentEntity.getDisplay());
        appointment.setUuid(appointmentEntity.getUuid());

        return appointment;
    }

    public static AppointmentEntity mapModelToEntity(Appointment appointment,String date) {
        if (appointment == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDisplay(appointment.getDisplay());
        appointmentEntity.setUuid(appointment.getUuid());
        appointmentEntity.setDate(Utility.getDateFromString(date));
        Log.d("sahil", "mapModelToEntity: "+Utility.getDateFromString(date));

        return appointmentEntity;
    }

    public static List<AppointmentEntity> mapModelToEntity(List<Appointment> appointmentCollection, String date) {
        List<AppointmentEntity> appointmentEntityCollection;

        if (appointmentCollection != null && !appointmentCollection.isEmpty()) {
            appointmentEntityCollection = new ArrayList<>();
            for (Appointment appointment : appointmentCollection) {
                appointmentEntityCollection.add(mapModelToEntity(appointment, date));
            }
        } else {
            appointmentEntityCollection = Collections.emptyList();
        }

        return appointmentEntityCollection;
    }
}