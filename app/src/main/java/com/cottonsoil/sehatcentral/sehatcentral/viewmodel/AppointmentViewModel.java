package com.cottonsoil.sehatcentral.sehatcentral.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.AppExecutors;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.SehatCentralDatabase;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentDetailsEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.SehatNetworkDataSource;
import com.cottonsoil.sehatcentral.sehatcentral.data.repository.SehatCentralRepository;

import java.util.List;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.DEBUG;

public class AppointmentViewModel extends AndroidViewModel {
    private static final String TAG = AppointmentViewModel.class.getSimpleName();

    private LiveData<List<AppointmentDetailsEntity>> mAppointment;

    private SehatCentralRepository mSehatCentralRepository;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        SehatCentralDatabase database = SehatCentralDatabase.getInstance(getApplication());
        AppExecutors executors = AppExecutors.getInstance();
        SehatNetworkDataSource networkDataSource = new SehatNetworkDataSource(getApplication());
        //SehatNetworkDataSource.getInstance(getApplication(), executors);
        mSehatCentralRepository = SehatCentralRepository.getInstance(database.appointmentListDao(), database.appointmentDetailDao(),
                networkDataSource, executors);
        mAppointment = mSehatCentralRepository.getMediatorAppointmentDetailsList();
    }

    @Override
    protected void onCleared() {
        if(DEBUG) Log.d(TAG, "onCleared: ");
        super.onCleared();
    }

    public LiveData<List<AppointmentDetailsEntity>> getAppointmentDetails() {
        return mAppointment;
    }
}
