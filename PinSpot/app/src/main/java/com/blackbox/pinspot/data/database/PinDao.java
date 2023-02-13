package com.blackbox.pinspot.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.blackbox.pinspot.model.Pin;

import java.util.List;

@Dao
public interface PinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Pin pin);

    @Delete
    void delete(Pin pin);

    @Query("DELETE FROM pin_table")
    void deleteAll();

    @Query("SELECT * FROM pin_table")
    List<Pin> getAllFavPin();
}
