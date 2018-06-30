package com.cottonsoil.sehatcentral.sehatcentral.data.mappers;

import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.Utility;
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
        appointmentDetailsEntity.setStartDate(Utility.parserTime(appointmentDetails.getTimeSlot().getStartDate()));
        appointmentDetailsEntity.setEndDate(Utility.parserTime(appointmentDetails.getTimeSlot().getEndDate()));
        appointmentDetailsEntity.setUuid(appointmentDetails.getUuid());
        appointmentDetailsEntity.setDisplayStatus(appointmentDetails.getDisplay());
        appointmentDetailsEntity.setPatientUuid(appointmentDetails.getPaitient().getUuid());
        appointmentDetailsEntity.setPatientAge(appointmentDetails.getPaitient().getPerson().getAge());
        appointmentDetailsEntity.setPatientGender(appointmentDetails.getPaitient().getPerson().getGender());
        appointmentDetailsEntity.setPatientName(appointmentDetails.getPaitient().getPerson().getName());
        Log.d("sahil","appointmentDetails.getDate()"+appointmentDetails.getDate());
        appointmentDetailsEntity.setDate(appointmentDetails.getDate());

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