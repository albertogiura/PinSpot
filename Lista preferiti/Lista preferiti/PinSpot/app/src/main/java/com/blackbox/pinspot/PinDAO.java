package com.blackbox.pinspot;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.blackbox.pinspot.model.Pin;

import java.util.List;

@Dao
public interface PinDAO {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Pin pin);

    @Query("DELETE FROM Pin_table")
    void deleteAll();

    @Query("SELECT * FROM Pin_table")
    LiveData<List<Pin>> getAllPin();
}
