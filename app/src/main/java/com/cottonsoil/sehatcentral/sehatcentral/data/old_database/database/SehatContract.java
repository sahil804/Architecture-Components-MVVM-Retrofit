package com.cottonsoil.sehatcentral.sehatcentral.data.old_database.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class SehatContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "com.cottonsoil.sehatcentral";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for Sunshine.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_APPOINTMENT_LIST = "appointment_list";

    public static final class AppointmentList implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_APPOINTMENT_LIST)
                .build();

        public static final String TABLE_NAME = "appointment_list";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_UUID = "uuid";

        public static final String COLUMN_DISPLAY = "display";
    }

    public static final String PATH_APPOINTMENT_DETAILS = "appointment_details";
    public static final class AppointmentDetails implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_APPOINTMENT_DETAILS)
                .build();

        public static final String TABLE_NAME = "appointment_details";

        public static final String COLUMN_DISPLAY = "display";

        public static final String COLUMN_PERSON_AGE = "age";

        public static final String COLUMN_PERSON_NAME = "name";

        public static final String COLUMN_PERSON_GENDER = "gender";

        public static final String COLUMN_PERSON_TIME_SLOT_DISPLAY = "time_slot_display";

        public static final String COLUMN_START_DATE = "start_date";

        public static final String COLUMN_END_DATE = "end_date";

        public static final String COLUMN_PATIENT_UUID = "patient_uuid";

        public static final String COLUMN_LOAD_DATE = "load_date";
    }

    public static final String PATH_PATIENT = "paitent";
    public static final class PATIENT implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PATIENT)
                .build();

        public static final String TABLE_NAME = "patient";

        public static final String COLUMN_PATIENT_NAME = "patientName";

        public static final String COLUMN_PATIENT_AGE = "age";

        public static final String COLUMN_PATIENT_GENDER = "gender";

        public static final String COLUMN_PATIENT_UUID = "patient_uuid";

        public static final String COLUMN_LOAD_DATE = "load_date";

    }
}