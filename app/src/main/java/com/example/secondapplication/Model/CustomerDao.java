package com.example.secondapplication.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.secondapplication.Entities.Customer;

import java.util.List;

public interface CustomerDao {
    @Insert
    void insert(Customer customer);

    @Update
    void update(Customer customer);

    @Delete
    void delete(Customer customer);

    @Query("DELETE FROM customer_table")
    void deleteAllCustomers();

    @Query("SELECT * FROM customer_table ORDER BY lastName DESC")
    LiveData<List<Customer>> getAllCustomers();
}
