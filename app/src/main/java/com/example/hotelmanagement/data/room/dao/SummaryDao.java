package com.example.hotelmanagement.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hotelmanagement.data.room.entity.Summary;

import java.util.List;

@Dao
public interface SummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(Summary summary);

    @Query("SELECT * from summary WHERE orderId = :userId")
    List<Summary> getSummaryByUserId(String userId);

}
