package com.cottonsoil.sehatcentral.sehatcentral.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.AppExecutors;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.dao.AppointmentDetailDao;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.dao.AppointmentListDao;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentDetailsEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.mappers.AppointmentDetailMapper;
import com.cottonsoil.sehatcentral.sehatcentral.data.mappers.AppointmentMapper;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.SehatNetworkDataSource;

import java.util.List;

public class SehatCentralRepository {

    private static final Object LOCK = new Object();
    private static final String TAG = SehatCentralRepository.class.getSimpleName();
    private static SehatCentralRepository sInstance;
    private final AppointmentListDao mAppointmentListDao;
    private final AppointmentDetailDao mAppointmentDetailDao;
    private final MutableLiveData<List<AppointmentDetails>> appointmentDetailsList = new MutableLiveData<>();
    private final MediatorLiveData<List<AppointmentDetailsEntity>> mediatorAppointmentDetailsList = new MediatorLiveData<>();
    private final SehatNetworkDataSource mSehatNetworkDataSource;
    private final AppExecutors mExecutors;

    public MediatorLiveData<List<AppointmentDetailsEntity>> getMediatorAppointmentDetailsList() {
        intializedData();
        return mediatorAppointmentDetailsList;
    }

    private SehatCentralRepository(final AppointmentListDao appointmentListDao, AppointmentDetailDao appointmentDetailDao,
                                   SehatNetworkDataSource sehatNetworkDataSource, AppExecutors executors) {
        mAppointmentListDao = appointmentListDao;
        mAppointmentDetailDao = appointmentDetailDao;
        mSehatNetworkDataSource = sehatNetworkDataSource;
        mExecutors = executors;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<List<Appointment>> appointmentListLiveData = mSehatNetworkDataSource.getAppointmentList();
        appointmentListLiveData.observeForever(newappointmentListFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                boolean isNewAppointment = false;
                List<AppointmentEntity> appointmentList = appointmentListDao.getAllAppointmentListStatic();
                if (appointmentList == null || (appointmentList != null && appointmentList.size() == 0)) {
                    isNewAppointment = true;
                }
                Log.d(TAG, "change detected in appointmentListLiveData " + appointmentList);
                for (AppointmentEntity appointment : appointmentList) {
                    AppointmentEntity storedAppointment = appointmentListDao.getAppointmentByUuid(appointment.getUuid());
                    if (storedAppointment == null) {
                        Log.d(TAG, "New Appointment");
                        isNewAppointment = true;
                        break;
                    } else if (!storedAppointment.getDisplay().equalsIgnoreCase(appointment.getDisplay())) {
                        Log.d(TAG, String.format("stored display = %s, new display = %s", storedAppointment.getDisplay(),
                                appointment.getDisplay()));
                        isNewAppointment = true;
                        break;
                    }
                }
                if (isNewAppointment) {
                    Log.d(TAG, "Old List deleted");
                    mAppointmentListDao.deleteAll();
                    mAppointmentListDao.insertAllAppointments(AppointmentMapper.mapModelToEntity(newappointmentListFromNetwork));
                }
                Log.d(TAG, "New values inserted");
            });
        });
        // TODO: 6/23/2018  below call should be from service
        mSehatNetworkDataSource.fetchAppointmentList();
    }

    public synchronized static SehatCentralRepository getInstance(
            AppointmentListDao appointmentListDao, AppointmentDetailDao appointmentDetailDao,
            SehatNetworkDataSource sehatNetworkDataSource, AppExecutors executors) {
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new SehatCentralRepository(appointmentListDao, appointmentDetailDao,
                        sehatNetworkDataSource, executors);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public void intializedData() {
        /*initializeData();
        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();*/
        LiveData<List<AppointmentDetails>> appointmentDetailsList =
                Transformations.switchMap(mAppointmentListDao.getAllAppointmentList(), (address) -> {
                    Log.d(TAG, "change detected in getAppointmentDetailsList "+address);
                    mExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mSehatNetworkDataSource.getAppointmentDetailsList(address);
                        }
                    });
                    return mSehatNetworkDataSource.getAppointmentDetails();
                });
        mediatorAppointmentDetailsList.addSource(appointmentDetailsList, appointmentDetailsListChanged -> {
            Log.d(TAG, "change detected in appointmentDetailsList "+appointmentDetailsListChanged);
            mExecutors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mAppointmentDetailDao.deleteAll();
                    mAppointmentDetailDao.insertAllAppointmentDetails(AppointmentDetailMapper.mapModelToEntity(appointmentDetailsListChanged));
                    mediatorAppointmentDetailsList.postValue(mAppointmentDetailDao.getAllAppointmentDetailsListStatic());
                }
            });

        });

        mediatorAppointmentDetailsList.addSource(mAppointmentDetailDao.getAllAppointmentDetailsList(), appointmentDetailsListChanged -> {
            Log.d(TAG, "change detected in mAppointmentDetailDao.getAllAppointmentDetailsList()"+appointmentDetailsListChanged);
           /* mExecutors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mediatorAppointmentDetailsList.postValue(appointmentDetailsListChanged);
                }
            });*/
            mediatorAppointmentDetailsList.setValue(appointmentDetailsListChanged);

        });
    }
}
