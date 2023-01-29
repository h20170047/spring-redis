package com.svj.service;

import com.svj.hash.Customer;
import com.svj.repository.CustomerDAO;
import com.svj.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerService {
    private CustomerDAO dao;
    private CustomerRepository repository;

    public CustomerService(CustomerDAO customerDAO, CustomerRepository customerRepository){
        dao= customerDAO;
        this.repository= customerRepository;
    }

    public Customer saveCustomer(Customer customer){
//        return dao.addCustomer(customer);
        return repository.save(customer);
    }

    public List<Customer> getAllCustomers(){
//        return dao.getAllCustomers();
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Customer getCustomerById(int id){
//        return dao.getCustomerById(id);
        return repository.findById(id).orElseThrow(()-> new RuntimeException("Customer not found with given ID"));
    }

    public String deleteCustomerById(int id){
//        return dao.deleteCustomerById(id);
        repository.deleteById(id);
        return "Customer removed!";
    }

    public Customer updateCustomerById(int id, Customer customer){
//        return dao.updateCustomerById(id,customer);
        Customer existingCustomer = repository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with given ID"));
        existingCustomer.setDob(customer.getDob());
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());
        return repository.save(existingCustomer);
    }

}
