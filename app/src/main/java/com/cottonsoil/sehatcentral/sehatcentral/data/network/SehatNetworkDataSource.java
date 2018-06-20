package com.cottonsoil.sehatcentral.sehatcentral.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SehatNetworkDataSource {

    public static final String TAG = SehatNetworkDataSource.class.getSimpleName();
    Context appContext;
    String accessToken;
    String provider;

    private final MutableLiveData<AppointmentList> appointmentList = new MutableLiveData<>();
    private final MutableLiveData<List<AppointmentDetails>> appointmentDetails = new MutableLiveData<>();
    public SehatNetworkDataSource(Context appContext) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(appContext);
        accessToken = mSettings.getString("accessToken", null);
        provider = mSettings.getString("provider", null);
        Log.d(TAG,"accessToken: "+accessToken + " provider: "+provider);
    }

    public MutableLiveData<AppointmentList> getAppointmentList() {
        return appointmentList;
    }

    public void fetchAppointmentList() {
        ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
        Call<AppointmentList> authorizationCall = null;
        try {
            authorizationCall = apiInterface.getAppointmentList(URLDecoder.decode(accessToken, "UTF-8"), provider);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        authorizationCall.enqueue(new Callback<AppointmentList>() {
            @Override
            public void onResponse(Call<AppointmentList> call, Response<AppointmentList> response) {
                AppointmentList list = response.body();
                Log.i(TAG, "onResponse: " + list);
                if(list != null) {
                    for (Appointment appointment :
                            list.getAppointments()) {
                        Log.i(TAG, "onResponse: " + appointment);
                    }
                    appointmentList.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<AppointmentList> call, Throwable t) {

            }
        });
    }

    public List<AppointmentDetails> getAppointmentDetailsList(AppointmentList listAppointment) {
        ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
        Call<AppointmentDetails> authorizationCall = null;
        try {
            List<AppointmentDetails> appointmentDetailsList = new ArrayList<>();
            for (Appointment appoint: listAppointment.getAppointments()) {
                authorizationCall = apiInterface.getAppointment(URLDecoder.decode(accessToken, "UTF-8"), appoint.getUuid());
                Response<AppointmentDetails> response = authorizationCall.execute();
                /*authorizationCall.enqueue(new Callback<AppointmentDetails>() {
                    @Override
                    public void onResponse(Call<AppointmentDetails> call, Response<AppointmentDetails> response) {
                        AppointmentDetails details = response.body();
                        Log.i(TAG, "onResponse: " + details);
                        appointmentDetailsList.add(details);
                    }

                    @Override
                    public void onFailure(Call<AppointmentDetails> call, Throwable t) {

                    }
                });*/
                appointmentDetailsList.add(response.body());
            }
            return appointmentDetailsList;
            //appointmentDetails.setValue(response.);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
