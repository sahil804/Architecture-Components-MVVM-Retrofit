package com.cottonsoil.sehatcentral.sehatcentral.scheduler;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.cottonsoil.sehatcentral.sehatcentral.Utility;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.SehatNetworkDataSource;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AppointmentListSync extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_APPOINTMENT_LIST_SYNC = "com.cottonsoil.sehatcentral.sehatcentral.scheduler.action";
    SehatNetworkDataSource mSehatNetworkDataSource;

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.cottonsoil.sehatcentral.sehatcentral.scheduler.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.cottonsoil.sehatcentral.sehatcentral.scheduler.extra.PARAM2";

    public AppointmentListSync() {
        super("AppointmentListSync");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_APPOINTMENT_LIST_SYNC.equals(action)) {
                mSehatNetworkDataSource.fetchAppointmentList(Utility.getTodayDateInString());
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSehatNetworkDataSource = SehatNetworkDataSource.getInstance(getApplication());
    }
}
