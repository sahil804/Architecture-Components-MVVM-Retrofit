package com.cottonsoil.sehatcentral.sehatcentral.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.AppExecutors;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.SehatCentralDatabase;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.SehatNetworkDataSource;
import com.cottonsoil.sehatcentral.sehatcentral.data.repository.SehatCentralRepository;

import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {
    private static final String TAG = AppointmentViewModel.class.getSimpleName();

    private LiveData<List<Appointment>> mAppointment;

    private SehatCentralRepository mSehatCentralRepository;
    
    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        SehatCentralDatabase database = SehatCentralDatabase.getInstance(getApplication());
        AppExecutors executors = AppExecutors.getInstance();
        SehatNetworkDataSource networkDataSource = new SehatNetworkDataSource(getApplication());
                //SehatNetworkDataSource.getInstance(getApplication(), executors);
        mSehatCentralRepository = new SehatCentralRepository(database.appointmentListDao(), database.appointmentDetailDao(),
                networkDataSource, executors);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared: ");
        super.onCleared();
    }

    public LiveData<List<AppointmentDetails>>getAppointmentDetails() {
        return mSehatCentralRepository.getAppointmentDetailsList();
    }
}
