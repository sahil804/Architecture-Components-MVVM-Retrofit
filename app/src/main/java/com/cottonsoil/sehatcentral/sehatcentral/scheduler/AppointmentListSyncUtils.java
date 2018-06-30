package com.cottonsoil.sehatcentral.sehatcentral.scheduler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobTrigger;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by sahil on 5/14/2018.
 */
public class AppointmentListSyncUtils {
    private static final int SYNC_INTERVAL_MINUTES = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_MINUTES);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String AppointmentList_SYNC_TAG = "appointment-sync";

    private static boolean sInitialized;
    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job syncAppointmentListJob = dispatcher.newJobBuilder()
                .setService(AppointmentListJobService.class)
                .setTag(AppointmentList_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(periodicTrigger(60, 1))
                //.setTrigger(Trigger.executionWindow(30,  60))
                //.setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true).build();
        int value = dispatcher.schedule(syncAppointmentListJob);
        Log.d("AppointmentListSync","schedule value is"+value);
    }

    public static JobTrigger periodicTrigger(int frequency, int tolerance) {
        return Trigger.executionWindow(frequency - tolerance, frequency);
    }

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInitialized) return;
        sInitialized = true;
        Log.d("AppointmentListSync","initialize");
        scheduleFirebaseJobDispatcherSync(context);
    }
}

