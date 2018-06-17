package com.cottonsoil.sehatcentral.sehatcentral.repository;

import com.cottonsoil.sehatcentral.sehatcentral.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.models.AppointmentList;
import com.cottonsoil.sehatcentral.sehatcentral.models.Authorization;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("ws/rest/v1/login")
    Call<Authorization> loginAccount(@Header("Authorization") String authKey);

    @GET("ws/rest/v1/appointmentscheduling/appointment")
    Call<AppointmentList> getAppointmentList(@Header("accesstoken") String accesstoken, @Query("provider") String provider);

    @GET("ws/rest/v1/appointmentscheduling/appointment/{uri}")
    Call<AppointmentDetails> getAppointment(@Header("accesstoken") String accesstoken, @Path("uri")String uri);
}