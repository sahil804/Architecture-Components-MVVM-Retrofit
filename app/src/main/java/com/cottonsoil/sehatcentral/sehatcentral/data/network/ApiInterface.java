package com.cottonsoil.sehatcentral.sehatcentral.data.network;

import com.cottonsoil.sehatcentral.sehatcentral.data.models.ActiveVisit;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.ActiveVisitDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
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

    @GET("ws/rest/v1/login")
    Call<Authorization> loginAccount(@Header("Authorization") String authKey);

    @GET("ws/rest/v1/appointmentscheduling/appointment")
    Call<List<Appointment>> getAppointmentList(@Header("accesstoken") String accesstoken, @Query("provider") String provider);

    @GET("ws/rest/v1/appointmentscheduling/appointment/{uri}")
    Call<AppointmentDetails> getAppointment(@Header("accesstoken") String accesstoken, @Path("uri")String uri);

    @GET("ws/rest/v1/visit")
    Call<List<ActiveVisit>>getPatientActiveVisit(@Query("patient") String patientUuid, @Query("includeInactive") boolean includeInActive);

    @GET("ws/rest/v1/visit/{uri}")
    Call<ActiveVisitDetails>getPatientActiveVisitDetails(@Path("uri")String uri);

    @GET("ws/rest/v1/visit/{uri}")
    Call<VitalsEncounter>getPatientActiveVisitVitals(@Path("uri")String uri);

    @GET("ws/rest/v1/visit/{uri}")
    Call<PrescriptionEncounter>getPatientActiveVisitPrescription(@Path("uri")String uri);


}