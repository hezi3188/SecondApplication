package com.example.secondapplication.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.secondapplication.Entities.Parcel;

import java.util.List;

@Dao
public interface ParcelDao {
    @Insert
    void insert(Parcel parcel);

    @Update
    void update(Parcel parcel);

    @Delete
    void delete(Parcel parcel);

    @Query("DELETE FROM parcel_table")
    void deleteAllParcels();

    @Query("SELECT * FROM parcel_table Where status<>'ACCEPTED' ORDER BY getParcelDate DESC")
    LiveData<List<Parcel>> getAllParcelsUserNotAccepted();

    @Query("SELECT * FROM parcel_table Where status='ACCEPTED' ORDER BY getParcelDate DESC")
    LiveData<List<Parcel>> getAllParcelsUserAccepted();
}