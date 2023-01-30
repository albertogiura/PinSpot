package com.blackbox.pinspot.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavouritesDAO {

    @Insert
    void insert(Favourties... favourties);

    @Query("SELECT * FROM Favourties")
    List<Favourties> returnAllFavourites();
}
