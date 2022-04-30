package com.example.hotelmanagement.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hotelmanagement.data.room.dao.SummaryDao;
import com.example.hotelmanagement.data.room.entity.Summary;

@Database(entities = {Summary.class}, version = 1)
public abstract class SummaryDatabase extends RoomDatabase {
    private static SummaryDatabase instance;

    public abstract SummaryDao orderDao();

    public static SummaryDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance =
                    Room.databaseBuilder(context.getApplicationContext(), SummaryDatabase.class, "summary-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }


}
