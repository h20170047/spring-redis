package com.svj.service;

import com.svj.hash.Customer;
import com.svj.repository.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerDAO dao;

    public CustomerService(CustomerDAO customerDAO){
        dao= customerDAO;
    }

    public Customer saveCustomer(Customer customer){
        return dao.addCustomer(customer);
    }

    public List<Customer> getAllCustomers(){
        return dao.getAllCustomers();
    }

    public Customer getCustomerById(int id){
        return dao.getCustomerById(id);
    }

    public String deleteCustomerById(int id){
        return dao.deleteCustomerById(id);
    }

    public Customer updateCustomerById(int id, Customer customer){
        return dao.updateCustomerById(id,customer);
    }

}
