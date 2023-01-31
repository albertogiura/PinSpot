package com.blackbox.pinspot;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.blackbox.pinspot.model.Pin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Pin.class}, version = 1, exportSchema = false)
public abstract class PinRoomDB extends RoomDatabase {

    public abstract PinDAO pinDao();

    private static volatile PinRoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PinRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PinRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PinRoomDB.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
