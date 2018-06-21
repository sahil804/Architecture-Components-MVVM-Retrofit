package com.cottonsoil.sehatcentral.sehatcentral.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.AppExecutors;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.dao.AppointmentDetailDao;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.dao.AppointmentListDao;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentList;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.SehatNetworkDataSource;

import java.util.List;

public class SehatCentralRepository {

    private static final Object LOCK = new Object();
    private static final String LOG_TAG = SehatCentralRepository.class.getSimpleName() ;
    private static SehatCentralRepository sInstance;
    private final AppointmentListDao mAppointmentListDao;
    private final AppointmentDetailDao mAppointmentDetailDao;
    //private final MutableLiveData<AppointmentList> appointmentList = new MutableLiveData<>();
    private final MutableLiveData<List<AppointmentDetails>> appointmentDetailsList = new MutableLiveData<>();
    private final MediatorLiveData<List<AppointmentDetails>> mediatorAppointmentDetailsList = new MediatorLiveData<>();
    private final SehatNetworkDataSource mSehatNetworkDataSource;
    private final AppExecutors mExecutors;

    public SehatCentralRepository(final AppointmentListDao appointmentListDao, AppointmentDetailDao appointmentDetailDao,
                                  SehatNetworkDataSource sehatNetworkDataSource, AppExecutors executors) {
        mAppointmentListDao = appointmentListDao;
        mAppointmentDetailDao = appointmentDetailDao;
        mSehatNetworkDataSource = sehatNetworkDataSource;
        mExecutors = executors;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<AppointmentList> appointmentListLiveData = mSehatNetworkDataSource.getAppointmentList();
        appointmentListLiveData.observeForever(newappointmentListFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                boolean isNewAppointment = false;
                AppointmentList appointmentList = appointmentListDao.getAllAppointmentListStatic();
                for (Appointment appointment: appointmentList.getAppointments()) {
                    Appointment storedAppointment = appointmentListDao.getAppointmentByUuid(appointment.getUuid());
                    if(storedAppointment == null) {
                        Log.d(LOG_TAG, "New Appointment");
                        isNewAppointment = true;
                        break;
                    } else if(!storedAppointment.getDisplay().equalsIgnoreCase(appointment.getDisplay())) {
                        Log.d(LOG_TAG, String.format("stored display = %s, new display = %s", storedAppointment.getDisplay(),
                                appointment.getDisplay()));
                        isNewAppointment = true;
                        break;
                    }
                }
                if(isNewAppointment) {
                    Log.d(LOG_TAG, "Old List deleted");
                    mAppointmentListDao.deleteAll();
                    mAppointmentListDao.insertAllAppointments(newappointmentListFromNetwork);
                }

                // Insert our new weather data into Sunshine's database
                //mWeatherDao.bulkInsert(newappointmentListFromNetwork);
                Log.d(LOG_TAG, "New values inserted");
            });
        });
    }

    public synchronized static SehatCentralRepository getInstance(
            AppointmentListDao appointmentListDao,  AppointmentDetailDao appointmentDetailDao,
            SehatNetworkDataSource sehatNetworkDataSource, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new SehatCentralRepository(appointmentListDao, appointmentDetailDao,
                        sehatNetworkDataSource, executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public LiveData<List<AppointmentDetails>> getAppointmentDetailsList() {
        /*initializeData();
        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();*/
        /*LiveData<List<AppointmentDetails>> appointmentDetailsList =
                Transformations.switchMap(mAppointmentListDao.getAllAppointmentList(), (address) -> {
                    return mSehatNetworkDataSource.getAppointmentDetailsList(mAppointmentListDao.getAllAppointmentListStatic());
                });*/
        mediatorAppointmentDetailsList.addSource(mAppointmentListDao.getAllAppointmentList(), appointmentList -> {
            mExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    mAppointmentDetailDao.insertAllAppointmentDetails(mSehatNetworkDataSource.getAppointmentDetailsList(appointmentList));
                    mediatorAppointmentDetailsList.postValue(mAppointmentDetailDao.getAllAppointmentDetailsListStatic());
                }
            });
        });
        return mediatorAppointmentDetailsList;
    }
}
