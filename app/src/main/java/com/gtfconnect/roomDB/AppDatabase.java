package com.gtfconnect.roomDB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gtfconnect.models.channelDashboardModels.ChannelDashboardDataModel;
import com.gtfconnect.models.exclusiveOfferResponse.ExclusiveOfferDataModel;
import com.gtfconnect.models.groupDashboardModels.GroupDashboardDataModel;
import com.gtfconnect.roomDB.convertors.CommonConvertor;
import com.gtfconnect.roomDB.convertors.GroupChannelInfoConvertor;
import com.gtfconnect.roomDB.dbEntities.channelChatDbEntities.ChannelChatBodyDbEntity;
import com.gtfconnect.roomDB.dbEntities.channelChatDbEntities.ChannelChatHeaderDbEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelGalleryEntity.GroupChannelGalleryEntity;
import com.gtfconnect.roomDB.dbEntities.groupChannelUserInfoEntities.InfoDbEntity;

@Database(entities = {ChannelDashboardDataModel.class, GroupDashboardDataModel.class, ChannelChatHeaderDbEntity.class, ChannelChatBodyDbEntity.class, ExclusiveOfferDataModel.class, GroupChannelGalleryEntity.class, InfoDbEntity.class}, version = 16)
@TypeConverters({CommonConvertor.class, GroupChannelInfoConvertor.class})

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

