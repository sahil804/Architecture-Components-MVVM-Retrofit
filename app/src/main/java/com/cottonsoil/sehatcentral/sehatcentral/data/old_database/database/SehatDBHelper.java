package com.cottonsoil.sehatcentral.sehatcentral.data.old_database.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Sahil on 4/20/2018.
 */
public class SehatDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sehat.db";
    private static final int DATABASE_VERSION = 1 ;
    /*public static final String TABLE_NAME = "Patient";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSON_DISPLAY = "display";
    public static final String COLUMN_PERSON_AGE = "age";
    public static final String COLUMN_PERSON_GENDER = "gender";
    public static final String COLUMN_PERSON_TIME_SLOT_DISPLAY = "time_slot_display";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";

    // UUID table
    public static final String TABLE_UUID = "Uuid";
    public static final String COLUMN_UUID_ID = "_id";
    public static final String COLUMN_UUID = "uuid";
    public static final String DATE = "date";*/


    public SehatDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Sahil","Oncreate");

        db.execSQL(" CREATE TABLE " + SehatContract.AppointmentDetails.TABLE_NAME + " (" +
                SehatContract.AppointmentDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SehatContract.AppointmentDetails.COLUMN_PERSON_NAME + " TEXT NOT NULL, " +
                SehatContract.AppointmentDetails.COLUMN_DISPLAY + " TEXT NOT NULL, " +
                SehatContract.AppointmentDetails.COLUMN_PERSON_AGE + " NUMBER NOT NULL, " +
                SehatContract.AppointmentDetails.COLUMN_PERSON_GENDER + " TEXT NOT NULL, " +
                SehatContract.AppointmentDetails.COLUMN_PERSON_TIME_SLOT_DISPLAY + " TEXT NOT NULL, " +
                SehatContract.AppointmentDetails.COLUMN_START_DATE + " TEXT NOT NULL, " +
                SehatContract.AppointmentDetails.COLUMN_PATIENT_UUID + " TEXT NOT NULL, " +
                SehatContract.AppointmentDetails.COLUMN_LOAD_DATE+ " TEXT NOT NULL, " +
                SehatContract.AppointmentDetails.COLUMN_END_DATE + " TEXT NOT NULL);"
        );

        db.execSQL(" CREATE TABLE " + SehatContract.AppointmentList.TABLE_NAME + " (" +
                SehatContract.AppointmentList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SehatContract.AppointmentList.COLUMN_DATE + " Text, " +
                SehatContract.AppointmentList.COLUMN_DISPLAY + " TEXT NOT NULL, "+
                SehatContract.AppointmentList.COLUMN_UUID + " TEXT UNIQUE ON CONFLICT REPLACE);"
        );

        db.execSQL(" CREATE TABLE " + SehatContract.PATIENT.TABLE_NAME + " (" +
                SehatContract.PATIENT._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SehatContract.PATIENT.COLUMN_PATIENT_NAME + " Text, " +
                SehatContract.PATIENT.COLUMN_PATIENT_GENDER + " Text, " +
                SehatContract.PATIENT.COLUMN_PATIENT_AGE + " TEXT NOT NULL, "+
                SehatContract.PATIENT.COLUMN_LOAD_DATE+ " TEXT NOT NULL, " +
                SehatContract.PATIENT.COLUMN_PATIENT_UUID + " TEXT UNIQUE ON CONFLICT REPLACE);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + SehatContract.AppointmentDetails.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SehatContract.AppointmentList.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SehatContract.PATIENT.TABLE_NAME);
        this.onCreate(db);
    }
    /**create record**/
    /*public void saveNewPatient(Appointment appointment) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_DISPLAY, appointment.getDisplay());
        values.put(COLUMN_PERSON_AGE, appointment.getAge());
        values.put(COLUMN_PERSON_GENDER, appointment.getGender());
        values.put(COLUMN_PERSON_TIME_SLOT_DISPLAY, appointment.getTimeslot_display());
        values.put(COLUMN_START_DATE, appointment.getStart_date());
        values.put(COLUMN_END_DATE, appointment.getEnd_date());

       // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    *//**Query records, give options to filter results**//*
    public List<Appointment> patientsList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<Appointment> patientsLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Appointment appointment;

        if (cursor.moveToFirst()) {
            do {
                appointment = new Appointment();

                appointment.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                appointment.setDisplay(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_DISPLAY)));
                appointment.setAge(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AGE)));
                appointment.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_GENDER)));
                appointment.setTimeslot_display(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_TIME_SLOT_DISPLAY)));
                appointment.setStart_date(cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE)));
                appointment.setEnd_date(cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE)));
                patientsLinkedList.add(appointment);
            } while (cursor.moveToNext());
        }


        return patientsLinkedList;
    }

    *//**Query only 1 record**//*
    public Appointment getPatient(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Appointment receivedAppointment = new Appointment();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedAppointment.setDisplay(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_DISPLAY)));
            receivedAppointment.setAge(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AGE)));
            receivedAppointment.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_GENDER)));
            receivedAppointment.setTimeslot_display(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_TIME_SLOT_DISPLAY)));
            receivedAppointment.setStart_date(cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE)));
            receivedAppointment.setEnd_date(cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE)));
        }

        return receivedAppointment;

    }


    *//**delete record**//*
    public void deletePatientRecord(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

   }

    *//**update record**//*
    public void updatePatientRecord(long personId, Context context, Appointment updatedAppointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+" SET name ='"+ updatedAppointment.getDisplay() + "', age ='" + updatedAppointment.getAge()+ "', occupation ='"+ updatedAppointment.getGender() + "', image ='"+ updatedAppointment.getTimeslot_display() + "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();


    }

    public int removeAllPatients(){
        SQLiteDatabase db = this.getWritableDatabase();

        int status = db.delete(TABLE_NAME, null, null);
        db.close();
        return status;
    }

    public int removeAllUuids(){
        SQLiteDatabase db = this.getWritableDatabase();

        int status = db.delete(TABLE_UUID, null, null);
        db.close();
        return status;
    }

    *//**create record**//*
    public void saveNewUuid(Uuid uuid) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, uuid.getDate());
        values.put(COLUMN_PERSON_DISPLAY, uuid.getDisplay());
        values.put(COLUMN_UUID, uuid.getUuid());

        // insert
        db.insert(TABLE_UUID,null, values);
        db.close();
    }



    public List<Uuid> getAllUuids() {
        List<Uuid> uuids = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_UUID ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Uuid uuid = new Uuid(cursor.getString(cursor.getColumnIndex(COLUMN_UUID)), cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_DISPLAY)),
                        cursor.getString(cursor.getColumnIndex(DATE)));

                uuids.add(uuid);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return uuids;
    }*/

}

