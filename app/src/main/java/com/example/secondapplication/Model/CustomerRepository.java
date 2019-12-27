package com.example.secondapplication.Model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.secondapplication.Entities.Customer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CustomerRepository {
    private CustomerDao customerDao;
    private DatabaseReference customersRef;

    private LiveData<List<Customer>> allCustomers;

    public CustomerRepository(Application application) {
        CustomerDatabase database = CustomerDatabase.getInstance(application);
        // Write a message to the database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        customersRef = firebaseDatabase.getReference("customers");

        customerDao = database.customerDao();

        allCustomers = customerDao.getAllCustomers();
    }

    public void insert(Customer customer) { new CustomerRepository.InsertCustomerAsyncTask(customerDao).execute(customer);}

    public void update(Customer customer) {
        new CustomerRepository.UpdateCustomerAsyncTask(customerDao).execute(customer);
    }

    public void delete(Customer customer) {
        new CustomerRepository.DeleteCustomerAsyncTask(customerDao).execute(customer);
    }

    public void deleteAllCustomers() {
        new CustomerRepository.DeleteAllCustomersAsyncTask(customerDao).execute();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    //region AsyncTask implementation

    private static class InsertCustomerAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao customerDao;

        private InsertCustomerAsyncTask(CustomerDao customerDao) {
            this.customerDao = customerDao;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            customerDao.insert(customers[0]);
            return null;
        }
    }

    private static class UpdateCustomerAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao customerDao;

        private UpdateCustomerAsyncTask(CustomerDao customerDao) {
            this.customerDao = customerDao;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            customerDao.update(customers[0]);
            return null;
        }
    }

    private static class DeleteCustomerAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDao customerDao;

        private DeleteCustomerAsyncTask(CustomerDao customerDao) {
            this.customerDao = customerDao;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            customerDao.delete(customers[0]);
            return null;
        }
    }

    private static class DeleteAllCustomersAsyncTask extends AsyncTask<Void, Void, Void> {
        private CustomerDao customerDao;

        private DeleteAllCustomersAsyncTask(CustomerDao customerDao) {
            this.customerDao = customerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            customerDao.deleteAllCustomers();
            return null;
        }
    }
}
