package com.cottonsoil.sehatcentral.sehatcentral.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.AppExecutors;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.SehatCentralDatabase;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentDetailsEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Encounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.SehatNetworkDataSource;
import com.cottonsoil.sehatcentral.sehatcentral.data.repository.SehatCentralRepository;

import java.util.List;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.DEBUG;

/**
 * Created by sahil on 6/27/2018.
 */
public class PatientEncountersViewModel extends AndroidViewModel {

    private static final String TAG = PatientEncountersViewModel.class.getSimpleName();

    private LiveData<List<Encounter>> mEncountersLiveData;

    private SehatCentralRepository mSehatCentralRepository;


    public PatientEncountersViewModel(@NonNull Application application) {
        super(application);
        if(DEBUG) Log.d(TAG, "PatientEncountersViewModel: ");
        SehatCentralDatabase database = SehatCentralDatabase.getInstance(getApplication());
        AppExecutors executors = AppExecutors.getInstance();
        SehatNetworkDataSource networkDataSource = new SehatNetworkDataSource(getApplication());
        //SehatNetworkDataSource.getInstance(getApplication(), executors);
        mSehatCentralRepository = SehatCentralRepository.getInstance(database.appointmentListDao(), database.appointmentDetailDao(),
                networkDataSource, executors);
        if(DEBUG) Log.d(TAG, "PatientEncountersViewModel: end ");
    }

    @Override
    protected void onCleared() {
        if(DEBUG) Log.d(TAG, "onCleared: ");
        super.onCleared();
    }

    public LiveData<List<Encounter>> getmEncountersLiveData(String uuid) {
        mSehatCentralRepository.getEncounters(uuid);
        mEncountersLiveData = mSehatCentralRepository.getEncounterListLiveData();
        return mEncountersLiveData;
    }
}