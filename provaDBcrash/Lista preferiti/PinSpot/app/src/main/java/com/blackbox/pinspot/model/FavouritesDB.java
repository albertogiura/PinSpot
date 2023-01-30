package com.blackbox.pinspot.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Favourties.class}, version = 1)
public abstract class FavouritesDB extends RoomDatabase {
    public abstract FavouritesDAO favouritesDAO();
}
