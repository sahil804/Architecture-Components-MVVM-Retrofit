package com.cottonsoil.sehatcentral.sehatcentral.data.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

public abstract class SehatCentralDatabase extends RoomDatabase{
    private static final String LOG_TAG = SehatCentralDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "sehatCentral";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static SehatCentralDatabase sInstance;

    public static SehatCentralDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        SehatCentralDatabase.class, SehatCentralDatabase.DATABASE_NAME).build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

}
