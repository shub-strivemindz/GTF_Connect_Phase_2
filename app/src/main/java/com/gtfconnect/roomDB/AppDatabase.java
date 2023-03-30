package com.gtfconnect.roomDB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gtfconnect.models.channelResponseModel.channelDashboardModels.ChannelResponseModel;

@Database(entities = {ChannelResponseModel.class}, version = 1)
@TypeConverters({Convertors.class})

public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase instance = null;

    public abstract AppDao appDao();


    public static AppDatabase getInstance(Application application) {
        synchronized (AppDatabase.class) {
            if (instance == null) {
                instance = Room.databaseBuilder(application, AppDatabase.class, "connect-service-provider.db").addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                    }
                }).fallbackToDestructiveMigration().build();
            }

            return instance;
        }
    }


}

