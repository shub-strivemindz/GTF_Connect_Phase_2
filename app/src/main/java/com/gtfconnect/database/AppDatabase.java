package com.gtfconnect.database;

/*
import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.gtfconnect.database.dao.GtfDao;
import com.gtfconnect.models.ProfileData;

@Database(entities = ProfileData.class, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "gtf_connect.db";
    public static AppDatabase instance;


    public static synchronized AppDatabase getInstance(Context context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return instance;
        }
    public abstract GtfDao appDao();
    }
*/
