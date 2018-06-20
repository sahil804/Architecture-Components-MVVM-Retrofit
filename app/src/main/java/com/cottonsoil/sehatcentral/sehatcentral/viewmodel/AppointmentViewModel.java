package com.cottonsoil.sehatcentral.sehatcentral.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;

import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {
    private static final String TAG = AppointmentViewModel.class.getSimpleName();

    private LiveData<List<Appointment>> mAppointment;
    
    public AppointmentViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared: ");
        super.onCleared();
    }
}
