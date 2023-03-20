package com.gtfconnect.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

public interface CurdOperationDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(T t);

    @Update
    void update(T t);

    @Delete
    void delete(T t);
}
