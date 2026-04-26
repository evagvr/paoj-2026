package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.exception.CustomerNotFoundException;
import com.pao.project.bankingApp.model.Customer;

import java.util.HashMap;
import java.util.TreeSet;

public class CustomerService {
    private TreeSet<Customer> customers;
    private HashMap<String, Customer> customersById;
    private static CustomerService instance = null;

    private CustomerService() {
        this.customers = new TreeSet<>();
        this.customersById = new HashMap<>();
    }

    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        customersById.put(customer.getUserId(), customer);
    }
    public Customer getCustomerById(String userId) throws CustomerNotFoundException{
        Customer customer = customersById.get(userId);
        if (customer == null) {
            throw new CustomerNotFoundException(userId);
        }
        return customer;
    }

    public Customer getCustomerByCnp(String cnp) {
        for (Customer customer : customers) {
            if (customer.getCnp().equals(cnp)) {
                return customer;
            }
        }
        throw new CustomerNotFoundException("CNP: " + cnp);
    }

    public TreeSet<Customer> getAllCustomers() {
        return customers;
    }
    public void updateCustomerEmail(String userId, String email) {
        Customer customer = getCustomerById(userId);
        customer.setEmail(email);
    }

    public void updateCustomerPhone(String userId, String phone) {
        Customer customer = getCustomerById(userId);
        customer.setPhone(phone);
    }
    public void removeCustomer(String userId) {
        Customer customer = getCustomerById(userId);
        customers.remove(customer);
        customersById.remove(userId);
    }

    public int getCustomerCount() {
        return customers.size();
    }
}
