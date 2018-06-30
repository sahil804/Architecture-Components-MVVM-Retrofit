package com.cottonsoil.sehatcentral.sehatcentral.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.Utility;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.ActiveVisit;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.ActiveVisitDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentList;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Encounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.PrescriptionEncounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.VitalsEncounter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.LongFunction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.DEBUG;
import static com.cottonsoil.sehatcentral.sehatcentral.Constants.PRESCRIPTIPN;
import static com.cottonsoil.sehatcentral.sehatcentral.Constants.VITALS;

public class SehatNetworkDataSource {

    public static final String TAG = SehatNetworkDataSource.class.getSimpleName();
    Context appContext;
    String accessToken;
    String provider;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static SehatNetworkDataSource sInstance;

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


    /**
     * Get the singleton for this class
     */
    public static SehatNetworkDataSource getInstance(Context context) {
        Log.d(TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new SehatNetworkDataSource(context.getApplicationContext());
                Log.d(TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public MutableLiveData<List<Appointment>> getAppointmentList() {
        return appointmentList;
    }

    public  MutableLiveData<List<Appointment>> fetchAppointmentList(String date) {
        if(date != null) {
            if (DEBUG)
                Log.d(TAG, "fetchAppointmentList: " + date + " next day:" + Utility.getNextDayDateInString(date));
            ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
            Call<AppointmentList> authorizationCall = null;
            try {
                authorizationCall = apiInterface.getAppointmentList(URLDecoder.decode(accessToken, "UTF-8"), provider,
                        /*"SCHEDULED",*/date, Utility.getNextDayDateInString(date));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            authorizationCall.enqueue(new Callback<AppointmentList>() {
                @Override
                public void onResponse(Call<AppointmentList> call, Response<AppointmentList> response) {
                    AppointmentList list = response.body();
                    if (DEBUG) Log.i(TAG, "onResponse: " + list);
                    if (DEBUG) Log.i(TAG, "onResponse size: " + list.getAppointments().size());
                    List<Appointment> appointments = list.getAppointments();
                    for (Appointment appointment : appointments) {
                        appointment.setDate(date);
                    }
                    appointmentList.setValue(appointments);
                    if (DEBUG) Log.i(TAG, "onResponse: appointments " + appointments);
                }

                @Override
                public void onFailure(Call<AppointmentList> call, Throwable t) {
                    if (DEBUG) Log.i(TAG, "onResponse failure: " + t.getLocalizedMessage());
                    t.printStackTrace();
                }
            });
            return appointmentList;
        } else {
            return null;
        }
    }

    public MutableLiveData<List<AppointmentDetails>> getAppointmentDetailsList(List<AppointmentEntity> listAppointment) {
        MutableLiveData<List<AppointmentDetails>> listMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
        Call<AppointmentDetails> authorizationCall = null;
        try {
            List<AppointmentDetails> appointmentDetailsList = new ArrayList<>();
            int i = 0;
            for (AppointmentEntity appoint : listAppointment) {
                authorizationCall = apiInterface.getAppointment(URLDecoder.decode(accessToken, "UTF-8"), appoint.getUuid());
                // Response<AppointmentDetails> response = authorizationCall.execute();
                authorizationCall.enqueue(new Callback<AppointmentDetails>() {
                    @Override
                    public void onResponse(Call<AppointmentDetails> call, Response<AppointmentDetails> response) {
                        AppointmentDetails details = response.body();
                        details.setDate(appoint.getDate());
                        if (DEBUG) Log.i(TAG, "onResponse: " + details);
                        appointmentDetailsList.add(details);
                        if(appointmentDetailsList.size() == listAppointment.size()) {
                            appointmentDetails.postValue(appointmentDetailsList);
                        }

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
            Call<ActiveVisit> activeVisitCall = apiInterface.getPatientActiveVisit(URLDecoder.decode(accessToken, "UTF-8"),uuid, false);
            Response<ActiveVisit> response = activeVisitCall.execute();
            Log.d(TAG, "getEncounters response: " + response.body());
            if (response != null && response.body().getVisits().size() > 0) {
                Log.d(TAG, "getEncounters: " + response.body());
                ActiveVisit.Visit activeVisit = response.body().getVisits().get(0);
                String uri = activeVisit.getUuid();
                Log.d(TAG, "getEncounters uri: " + uri);
                Call<ActiveVisitDetails> activeVisitDetailsCall = apiInterface.getPatientActiveVisitDetails(URLDecoder.decode(accessToken, "UTF-8"),uri);
                Response<ActiveVisitDetails> activeVisitDetailsResponse = activeVisitDetailsCall.execute();
                ActiveVisitDetails activeVisitDetails = activeVisitDetailsResponse.body();
                Log.d(TAG, "getEncounters activeVisitDetails: " + activeVisitDetails);
                List<Encounter> encounterList = activeVisitDetails.getEncounters();
                Log.d(TAG, "getEncounters encounterList: " + encounterList);
                int i = 0;
                if(encounterList != null) {
                    Iterator<Encounter> iter = encounterList.iterator();
                    //for (Encounter encounter : encounterList)
                    while (iter.hasNext()) {
                        Encounter encounter = iter.next();
                        if (encounter.getDisplay().contains(PRESCRIPTIPN)) {
                            Call<PrescriptionEncounter> prescriptionEncounterCall =
                                    apiInterface.getPatientActiveVisitPrescription(URLDecoder.decode(accessToken, "UTF-8"), encounter.getUuid());
                            Response<PrescriptionEncounter> prescriptionEncounterResponse =
                                    prescriptionEncounterCall.execute();
                            Log.d(TAG, "getEncounters prescriptionEncounterResponse: " + prescriptionEncounterResponse.body());
                            encounter = prescriptionEncounterResponse.body();
                            encounterList.set(i, encounter);
                        } else if (encounter.getDisplay().contains(VITALS)) {
                            Call<VitalsEncounter> vitalsEncounterCall =
                                    apiInterface.getPatientActiveVisitVitals(URLDecoder.decode(accessToken, "UTF-8"), encounter.getUuid());
                            Response<VitalsEncounter> vitalsEncounterResponse = vitalsEncounterCall.execute();
                            Log.d(TAG, "getEncounters vitalsEncounterResponse" + vitalsEncounterResponse.body());
                            encounter = vitalsEncounterResponse.body();
                            encounterList.set(i, encounter);
                        }
                        ++i;
                    }
                    iter = encounterList.iterator();
                    while (iter.hasNext()) {
                        Encounter encounter = iter.next();
                        if (!encounter.getDisplay().contains(PRESCRIPTIPN) && !encounter.getDisplay().contains(VITALS)) {
                            Log.d(TAG, "getEncounters: removing encounters other than prescription and vitals");
                            iter.remove();
                        }
                    }
                    encounterListLiveData.postValue(encounterList);
                } else {
                    Log.e(TAG, "getEncounters: encounterList "+encounterList );
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}