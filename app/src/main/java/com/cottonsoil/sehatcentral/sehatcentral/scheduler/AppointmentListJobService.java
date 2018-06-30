package com.cottonsoil.sehatcentral.sehatcentral.scheduler;

import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.Utility;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.SehatNetworkDataSource;;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.text.SimpleDateFormat;

/**
 * Created by sahil on 6/30/2018.
 */
public class AppointmentListJobService extends JobService {

    public static final String TAG = AppointmentListJobService.class.getSimpleName();
    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG,"onStartJob");
        SehatNetworkDataSource networkDataSource =
                SehatNetworkDataSource.getInstance(this.getApplicationContext());
        networkDataSource.fetchAppointmentList(Utility.getTodayDateInString());
        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG,"onStopJob");
        return true;
    }
}