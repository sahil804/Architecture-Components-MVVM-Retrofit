package com.cottonsoil.sehatcentral.sehatcentral.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
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

    public LiveData<String> getmDate() {
        return mDate;
    }

    public int getCurrentCounter() {
        return currentCounter;
    }

    public void setCurrentCounter(int currentCounter) {
        this.currentCounter = currentCounter;
    }

    int currentCounter = 0;

    private final MutableLiveData<String> mDate = new MutableLiveData();

    private LiveData<List<AppointmentDetailsEntity>> mAppointment;

    private SehatCentralRepository mSehatCentralRepository;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        SehatCentralDatabase database = SehatCentralDatabase.getInstance(getApplication());
        AppExecutors executors = AppExecutors.getInstance();
        SehatNetworkDataSource networkDataSource = SehatNetworkDataSource.getInstance(getApplication());
        //SehatNetworkDataSource.getInstance(getApplication(), executors);
        mSehatCentralRepository = SehatCentralRepository.getInstance(database.appointmentListDao(), database.appointmentDetailDao(),
                networkDataSource, executors, mDate);
        //mAppointment = Transformations.switchMap( mDate, (date)-> mSehatCentralRepository.getMediatorAppointmentDetailsList(date));
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

    public void setDate(String date) {
        Log.d(TAG, "setDate: "+date);
        mDate.setValue(date);
    }
}