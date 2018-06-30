package com.cottonsoil.sehatcentral.sehatcentral.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.cottonsoil.sehatcentral.sehatcentral.AppExecutors;
import com.cottonsoil.sehatcentral.sehatcentral.Utility;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.dao.AppointmentDetailDao;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.dao.AppointmentListDao;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentDetailsEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.mappers.AppointmentDetailMapper;
import com.cottonsoil.sehatcentral.sehatcentral.data.mappers.AppointmentMapper;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Encounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.SehatNetworkDataSource;
import com.cottonsoil.sehatcentral.sehatcentral.scheduler.AppointmentListSyncUtils;

import java.util.Date;
import java.util.List;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.DEBUG;
import static java.security.AccessController.getContext;

public class SehatCentralRepository {

    private static final Object LOCK = new Object();
    private static final String TAG = SehatCentralRepository.class.getSimpleName();
    private static SehatCentralRepository sInstance;
    private final AppointmentListDao mAppointmentListDao;
    private final AppointmentDetailDao mAppointmentDetailDao;
    private final LiveData<String> mDate;
    private final MediatorLiveData<List<AppointmentDetailsEntity>> mediatorAppointmentDetailsList = new MediatorLiveData<>();

    public LiveData<List<Encounter>> getEncounterListLiveData() {
        return encounterListLiveData;
    }

    LiveData<List<Encounter>> encounterListLiveData;
    private final SehatNetworkDataSource mSehatNetworkDataSource;
    private final AppExecutors mExecutors;

    public MediatorLiveData<List<AppointmentDetailsEntity>> getMediatorAppointmentDetailsList() {
        return mediatorAppointmentDetailsList;
    }

    private SehatCentralRepository(final AppointmentListDao appointmentListDao, AppointmentDetailDao appointmentDetailDao,
                                   SehatNetworkDataSource sehatNetworkDataSource, AppExecutors executors, LiveData<String> date) {
        mAppointmentListDao = appointmentListDao;
        mAppointmentDetailDao = appointmentDetailDao;
        mSehatNetworkDataSource = sehatNetworkDataSource;
        mExecutors = executors;
        mDate = date;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<List<Appointment>> appointmentListLiveData = Transformations.switchMap
                (date, (dateChange) -> mSehatNetworkDataSource.fetchAppointmentList(dateChange));
        if(appointmentListLiveData != null) {
            appointmentListLiveData.observeForever(newappointmentListFromNetwork -> {
                mExecutors.diskIO().execute(() -> {
                    observeAppointmentListChange(appointmentListDao, newappointmentListFromNetwork);
                });
            });
        }

        /*LiveData<List<AppointmentEntity>> databaseListLiveData = Transformations.switchMap(date,
                (datechange)-> mAppointmentListDao.getAllAppointmentListByDate(Utility.getDateFromString(datechange));*/
        LiveData<List<AppointmentEntity>> appointmentListLiveData1 = Transformations.switchMap
                (date, (dateChange) -> mAppointmentListDao.getAllAppointmentListByDate(Utility.getDateFromString(dateChange)));

        LiveData<List<AppointmentDetails>> appointmentDetailsList =
                Transformations.switchMap(appointmentListLiveData1, (appointmentEntityList) -> {
                    if (DEBUG)
                        Log.d(TAG, "change detected in getAppointmentDetailsList " + appointmentEntityList);
                    mExecutors.diskIO().execute(() -> mSehatNetworkDataSource.getAppointmentDetailsList(appointmentEntityList));
                    return mSehatNetworkDataSource.getAppointmentDetails();
                });

        mediatorAppointmentDetailsList.addSource(appointmentDetailsList, appointmentDetailsListChanged -> {
            if (DEBUG)
                Log.d(TAG, "change detected in appointmentDetailsList " + appointmentDetailsListChanged);
            mExecutors.diskIO().execute(() -> {
                if (appointmentDetailsListChanged.size() > 0) {
                    Date dateFound = appointmentDetailsListChanged.get(0).getDate();
                    mAppointmentDetailDao.deleteByDate(dateFound);
                    mAppointmentDetailDao.insertAllAppointmentDetails(AppointmentDetailMapper.mapModelToEntity(appointmentDetailsListChanged));
                    mediatorAppointmentDetailsList.postValue(mAppointmentDetailDao.getAppointmentDetailsListByDateStatic(dateFound));
                }
            });
        });

        LiveData<List<AppointmentDetailsEntity>> appointmentDetailsLiveDate = Transformations.switchMap
                (date, (dateChange) -> mAppointmentDetailDao.getAppointmentDetailsListByDate(Utility.getDateFromString(dateChange)));

        mediatorAppointmentDetailsList.addSource(appointmentDetailsLiveDate, appointmentDetailsListChanged -> {
            if (DEBUG)
                Log.d(TAG, "change detected in mAppointmentDetailDao.getAllAppointmentDetailsList()" + appointmentDetailsListChanged);
            mediatorAppointmentDetailsList.setValue(appointmentDetailsListChanged);
        });
    }

    private void observeAppointmentListChange(AppointmentListDao appointmentListDao, List<Appointment> newappointmentListFromNetwork) {
        boolean isNewAppointment = false;
        String date = null;
        if (newappointmentListFromNetwork.size() > 0) {
            date = newappointmentListFromNetwork.get(0).getDate();
        }
        if (date != null) {
            List<AppointmentEntity> appointmentList = appointmentListDao.
                    getAllAppointmentListStaticByDate(Utility.getDateFromString(String.valueOf(date)));
            if (DEBUG) Log.d(TAG, "Utility.getDateFromString(String.valueOf(date)) " +
                    Utility.getDateFromString(String.valueOf(date)));
            if (appointmentList == null || (appointmentList != null && appointmentList.size() == 0)) {
                isNewAppointment = true;
            }
            if (DEBUG) Log.d(TAG, "change detected in appointmentListLiveData " + appointmentList);
            for (AppointmentEntity appointment : appointmentList) {
                AppointmentEntity storedAppointment = appointmentListDao.getAppointmentByUuid(appointment.getUuid());
                if (storedAppointment == null) {
                    if (DEBUG) Log.d(TAG, "New Appointment");
                    isNewAppointment = true;
                    break;
                } else if (!storedAppointment.getDisplay().equalsIgnoreCase(appointment.getDisplay())) {
                    if (DEBUG)
                        Log.d(TAG, String.format("stored display = %s, new display = %s", storedAppointment.getDisplay(),
                                appointment.getDisplay()));
                    isNewAppointment = true;
                    break;
                }
            }
            if (isNewAppointment) {
                if (DEBUG) Log.d(TAG, "Old List deleted, new Value inserted");
                mAppointmentListDao.deleteByDate(Utility.getDateFromString(String.valueOf(date)));
                mAppointmentListDao.insertAllAppointments(AppointmentMapper.mapModelToEntity(newappointmentListFromNetwork, String.valueOf(date)));
            }
        }
    }

    public synchronized static SehatCentralRepository getInstance(
            AppointmentListDao appointmentListDao, AppointmentDetailDao appointmentDetailDao,
            SehatNetworkDataSource sehatNetworkDataSource, AppExecutors executors, LiveData<String> date) {
        if (DEBUG) Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new SehatCentralRepository(appointmentListDao, appointmentDetailDao,
                        sehatNetworkDataSource, executors, date);
                if (DEBUG) Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public void getEncounters(String uuid) {
        encounterListLiveData = mSehatNetworkDataSource.getEncounterListLiveData();
        mExecutors.diskIO().execute(() -> mSehatNetworkDataSource.getEncounters(uuid));
    }

    public AppointmentDetailsEntity getAppointmentDetailsByPatientUuid(String uuid) {
        return null;
    }
}