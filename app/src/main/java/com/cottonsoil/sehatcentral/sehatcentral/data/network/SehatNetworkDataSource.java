package com.cottonsoil.sehatcentral.sehatcentral.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.ActiveVisit;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.ActiveVisitDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Encounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.PrescriptionEncounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.VitalsEncounter;

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

    public MutableLiveData<List<Encounter>> getEncounterListLiveData() {
        return encounterListLiveData;
    }

    private final MutableLiveData<List<Encounter>> encounterListLiveData = new MutableLiveData<>();


    public SehatNetworkDataSource(Context appContext) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(appContext);
        accessToken = mSettings.getString("accessToken", null);
        provider = mSettings.getString("provider", null);
        if (DEBUG) Log.d(TAG, "accessToken: " + accessToken + " provider: " + provider);
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
                        if (DEBUG) Log.i(TAG, "onResponse: " + details);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void getEncounters(String uuid) {
        try {
            ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
            Call<List<ActiveVisit>> activeVisitCall = apiInterface.getPatientActiveVisit(uuid, false);
            Response<List<ActiveVisit>> response = activeVisitCall.execute();
            Log.d(TAG, "getEncounters response: " + response.body());
            if (response != null && response.body() != null && response.body().size() > 0) {
                Log.d(TAG, "getEncounters: " + response.body());
                ActiveVisit activeVisit = response.body().get(0);
                String uri = activeVisit.getLinks().get(0).getUri();
                Log.d(TAG, "getEncounters uri: " + uri);
                Call<ActiveVisitDetails> activeVisitDetailsCall = apiInterface.getPatientActiveVisitDetails(uri);
                Response<ActiveVisitDetails> activeVisitDetailsResponse = activeVisitDetailsCall.execute();
                ActiveVisitDetails activeVisitDetails = activeVisitDetailsResponse.body();
                List<Encounter> encounterList = activeVisitDetails.getEncounters();
                Log.d(TAG, "getEncounters encounterList: " + encounterList);
                for (Encounter encounter : encounterList) {
                    if (encounter.getDisplay().equalsIgnoreCase("Prescription")) {
                        Call<PrescriptionEncounter> prescriptionEncounterCall =
                                apiInterface.getPatientActiveVisitPrescription(encounter.getUuid());
                        Response<PrescriptionEncounter> prescriptionEncounterResponse =
                                prescriptionEncounterCall.execute();
                        Log.d(TAG, "getEncounters prescriptionEncounterResponse: " + prescriptionEncounterResponse.body());
                        encounter = prescriptionEncounterResponse.body();

                    }

                    if (encounter.getDisplay().equalsIgnoreCase("Vitals")) {
                        Call<VitalsEncounter> vitalsEncounterCall =
                                apiInterface.getPatientActiveVisitVitals(encounter.getUuid());
                        Response<VitalsEncounter> vitalsEncounterResponse = vitalsEncounterCall.execute();
                        Log.d(TAG, "getEncounters vitalsEncounterResponse" + vitalsEncounterResponse.body());
                        encounter = vitalsEncounterResponse.body();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
