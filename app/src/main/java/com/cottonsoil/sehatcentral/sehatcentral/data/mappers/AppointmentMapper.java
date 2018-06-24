package com.cottonsoil.sehatcentral.sehatcentral.data.mappers;

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

    public static AppointmentEntity mapModelToEntity(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDisplay(appointment.getDisplay());
        appointmentEntity.setUuid(appointment.getUuid());

        return appointmentEntity;
    }

    public static List<AppointmentEntity> mapModelToEntity(List<Appointment> appointmentCollection) {
        List<AppointmentEntity> appointmentEntityCollection;

        if (appointmentCollection != null && !appointmentCollection.isEmpty()) {
            appointmentEntityCollection = new ArrayList<>();
            for (Appointment appointment : appointmentCollection) {
                appointmentEntityCollection.add(mapModelToEntity(appointment));
            }
        } else {
            appointmentEntityCollection = Collections.emptyList();
        }

        return appointmentEntityCollection;
    }
}
