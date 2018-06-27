package com.cottonsoil.sehatcentral.sehatcentral.data.network;

import com.cottonsoil.sehatcentral.sehatcentral.data.models.ActiveVisit;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.ActiveVisitDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentList;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Authorization;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.PrescriptionEncounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.VitalsEncounter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/ws/rest/v1/login")
    Call<Authorization> loginAccount(@Header("Authorization") String authKey);

    @GET("/ws/rest/v1/appointmentscheduling/appointment")
    Call<AppointmentList> getAppointmentList(@Header("accesstoken") String accesstoken, @Query("provider") String provider,
            /*@Query("status") String status,*/ @Query("fromDate") String fromDate,
                                             @Query("toDate") String toDate);

    @GET("/ws/rest/v1/appointmentscheduling/appointment/{uri}")
    Call<AppointmentDetails> getAppointment(@Header("accesstoken") String accesstoken, @Path("uri")String uri);

    @GET("/ws/rest/v1/visit")
    Call<ActiveVisit>getPatientActiveVisit(@Header("accesstoken") String accesstoken,@Query("patient") String patientUuid, @Query("includeInactive") boolean includeInActive);

    @GET("/ws/rest/v1/visit/{uri}")
    Call<ActiveVisitDetails>getPatientActiveVisitDetails(@Header("accesstoken") String accesstoken,@Path("uri")String uri);

    @GET("/ws/rest/v1/encounter/{uri}")
    Call<VitalsEncounter>getPatientActiveVisitVitals(@Header("accesstoken") String accesstoken,@Path("uri")String uri);

    @GET("/ws/rest/v1/encounter/{uri}")
    Call<PrescriptionEncounter>getPatientActiveVisitPrescription(@Header("accesstoken") String accesstoken,@Path("uri")String uri);


}