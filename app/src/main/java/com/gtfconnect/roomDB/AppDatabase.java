package com.gtfconnect.roomDB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;

import com.gtfconnect.roomDB.convertors.CommonConvertor;
import com.gtfconnect.roomDB.convertors.DashboardDataConvertor;
import com.gtfconnect.roomDB.convertors.GroupChannelInfoConvertor;
import com.gtfconnect.roomDB.convertors.UserProfileDataConvertor;
import com.gtfconnect.roomDB.dbEntities.UserProfileDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatBodyDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelChatDbEntities.GroupChannelChatHeaderDbEntity;
import com.gtfconnect.roomDB.dbEntities.dashboardDbEntities.DashboardListEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

@Database(entities = {DashboardListEntity.class, GroupChannelChatHeaderDbEntity.class, GroupChannelChatBodyDbEntity.class, ExclusiveOfferDataModel.class, GroupChannelGalleryEntity.class, InfoDbEntity.class, UserProfileDbEntity.class}, version = 23)
@TypeConverters({CommonConvertor.class, GroupChannelInfoConvertor.class, DashboardDataConvertor.class, UserProfileDataConvertor.class})

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

