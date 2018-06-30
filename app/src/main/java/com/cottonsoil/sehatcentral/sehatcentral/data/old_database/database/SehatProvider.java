package com.cottonsoil.sehatcentral.sehatcentral.data.old_database.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class SehatProvider extends ContentProvider {

    public static final String TAG = SehatProvider.class.getSimpleName();
    public static final int  CODE_APPOINTMENT_LIST = 100;

    public static final int CODE_APPOINTMENT_DETAILS = 200;

    public static final int CODE_PATIENT = 300;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private SehatDBHelper mSehatDBHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SehatContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, SehatContract.PATH_APPOINTMENT_LIST, CODE_APPOINTMENT_LIST);
        matcher.addURI(authority, SehatContract.PATH_APPOINTMENT_DETAILS, CODE_APPOINTMENT_DETAILS);
        matcher.addURI(authority, SehatContract.PATH_PATIENT, CODE_PATIENT);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mSehatDBHelper = new SehatDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mSehatDBHelper.getReadableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_APPOINTMENT_LIST:
                Log.d(TAG,"query CODE_APPOINTMENT_LIST");
                retCursor = db.query(SehatContract.AppointmentList.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;


            case CODE_APPOINTMENT_DETAILS:
                Log.d(TAG,"query CODE_APPOINTMENT_DETAILS");
                retCursor = db.query(SehatContract.AppointmentDetails.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            case CODE_PATIENT:
                Log.d(TAG,"query CODE_PATIENT");
                retCursor = db.query(SehatContract.PATIENT.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            default: throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mSehatDBHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_APPOINTMENT_LIST:
                Log.d(TAG,"insert CODE_APPOINTMENT_LIST");
                long id = db.insert(SehatContract.AppointmentList.TABLE_NAME, null, contentValues);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(SehatContract.AppointmentList.CONTENT_URI, id);
                }
                break;


            case CODE_APPOINTMENT_DETAILS:
                Log.d(TAG,"insert CODE_APPOINTMENT_DETAILS");
                long id_details = db.insert(SehatContract.AppointmentDetails.TABLE_NAME, null, contentValues);
                if(id_details > 0) {
                    returnUri = ContentUris.withAppendedId(SehatContract.AppointmentDetails.CONTENT_URI, id_details);
                }
                break;

            case CODE_PATIENT:
                Log.d(TAG,"insert CODE_PATIENT");
                long id_patient = db.insert(SehatContract.PATIENT.TABLE_NAME, null, contentValues);
                if(id_patient > 0) {
                    returnUri = ContentUris.withAppendedId(SehatContract.PATIENT.CONTENT_URI, id_patient);
                }
                break;
            default: throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        // This is as per android doc, if selection is null, change it to 1.
        if(null == selection ) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_APPOINTMENT_LIST:
                Log.d(TAG,"delete CODE_APPOINTMENT_LIST");
                numRowsDeleted = mSehatDBHelper.getWritableDatabase().delete(
                        SehatContract.AppointmentList.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;


            case CODE_APPOINTMENT_DETAILS:
                Log.d(TAG,"delete CODE_APPOINTMENT_DETAILS");
                numRowsDeleted = mSehatDBHelper.getWritableDatabase().delete(
                        SehatContract.AppointmentDetails.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;

            case CODE_PATIENT:
                Log.d(TAG,"delete CODE_PATIENT");
                numRowsDeleted = mSehatDBHelper.getWritableDatabase().delete(
                        SehatContract.PATIENT.TABLE_NAME,
                        selection,
                        selectionArgs
                );

                break;

            default: throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        Log.d(TAG,"num of rows deleted: "+numRowsDeleted);
        if(numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsUpdated;

        // This is as per android doc, if selection is null, change it to 1.
        //if(null == selection ) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_APPOINTMENT_LIST:
                Log.d(TAG,"update CODE_APPOINTMENT_LIST");
                numRowsUpdated = mSehatDBHelper.getWritableDatabase().update(
                        SehatContract.AppointmentList.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;


            case CODE_APPOINTMENT_DETAILS:
                Log.d(TAG,"update CODE_APPOINTMENT_DETAILS");
                numRowsUpdated = mSehatDBHelper.getWritableDatabase().update(
                        SehatContract.AppointmentDetails.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);

                break;

            case CODE_PATIENT:
                Log.d(TAG,"update CODE_PATIENT");
                numRowsUpdated = mSehatDBHelper.getWritableDatabase().update(
                        SehatContract.PATIENT.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);

                break;


            default: throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        Log.d(TAG,"num of rows updated: "+numRowsUpdated);
        if(numRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsUpdated;
    }

    @Override
    public void shutdown() {
        mSehatDBHelper.close();
        super.shutdown();
    }
}