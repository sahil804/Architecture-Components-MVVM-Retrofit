package com.cottonsoil.sehatcentral.sehatcentral.data.mappers;

import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentDetailsEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppointmentDetailMapper {

    public static AppointmentDetailsEntity mapModelToEntity(AppointmentDetails appointmentDetails) {
        if (appointmentDetails == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final AppointmentDetailsEntity appointmentDetailsEntity = new AppointmentDetailsEntity();
        appointmentDetailsEntity.setStartDate(appointmentDetails.getTimeSlot().getStartDate());
        appointmentDetailsEntity.setEndDate(appointmentDetails.getTimeSlot().getEndDate());
        appointmentDetailsEntity.setUuid(appointmentDetails.getUuid());

        return appointmentDetailsEntity;
    }

    public static List<AppointmentDetailsEntity> mapModelToEntity(List<AppointmentDetails> appointmentDetailsCollection) {
        List<AppointmentDetailsEntity> appointmentDetailsEntitiesCollection;

        if (appointmentDetailsCollection != null && !appointmentDetailsCollection.isEmpty()) {
            appointmentDetailsEntitiesCollection = new ArrayList<>();
            for (AppointmentDetails appointmentDetails : appointmentDetailsCollection) {
                appointmentDetailsEntitiesCollection.add(mapModelToEntity(appointmentDetails));
            }
        } else {
            appointmentDetailsEntitiesCollection = Collections.emptyList();
        }

        return appointmentDetailsEntitiesCollection;
    }
}
