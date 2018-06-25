package com.cottonsoil.sehatcentral.sehatcentral.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.DEBUG;

public class SehatNetworkDataSource {

    public static final String TAG = SehatNetworkDataSource.class.getSimpleName();
    Context appContext;
    String accessToken;
    String provider;

    private final MutableLiveData<List<Appointment>> appointmentList = new MutableLiveData<>();

    public MutableLiveData<List<AppointmentDetails>> getAppointmentDetails() {
        return appointmentDetails;
    }

    private final MutableLiveData<List<AppointmentDetails>> appointmentDetails = new MutableLiveData<>();

    public SehatNetworkDataSource(Context appContext) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(appContext);
        accessToken = mSettings.getString("accessToken", null);
        provider = mSettings.getString("provider", null);
        if(DEBUG) Log.d(TAG, "accessToken: " + accessToken + " provider: " + provider);
    }

    public MutableLiveData<List<Appointment>> getAppointmentList() {
        return appointmentList;
    }

    public void fetchAppointmentList() {
        ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
        Call<List<Appointment>> authorizationCall = null;
        try {
            authorizationCall = apiInterface.getAppointmentList(URLDecoder.decode(accessToken, "UTF-8"), provider);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        authorizationCall.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                List<Appointment> list = response.body();
                if (DEBUG) Log.i(TAG, "onResponse: " + list);
                appointmentList.setValue(list);
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<AppointmentDetails>> getAppointmentDetailsList(List<AppointmentEntity> listAppointment) {
        MutableLiveData<List<AppointmentDetails>> listMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
        Call<AppointmentDetails> authorizationCall = null;
        try {
            List<AppointmentDetails> appointmentDetailsList = new ArrayList<>();
            int i = 0;
            for (AppointmentEntity appoint : listAppointment) {
                if (++i >= 10) {
                    break;
                }
                authorizationCall = apiInterface.getAppointment(URLDecoder.decode(accessToken, "UTF-8"), appoint.getUuid());
                // Response<AppointmentDetails> response = authorizationCall.execute();
                authorizationCall.enqueue(new Callback<AppointmentDetails>() {
                    @Override
                    public void onResponse(Call<AppointmentDetails> call, Response<AppointmentDetails> response) {
                        AppointmentDetails details = response.body();
                        if(DEBUG) Log.i(TAG, "onResponse: " + details);
                        appointmentDetailsList.add(details);
                        appointmentDetails.postValue(appointmentDetailsList);

                    }

                    @Override
                    public void onFailure(Call<AppointmentDetails> call, Throwable t) {

                    }
                });
            }
            //listMutableLiveData.setValue(appointmentDetailsList);

            return listMutableLiveData;
            //appointmentDetails.setValue(response.);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
