package com.svj.controller;

import com.svj.hash.Customer;
import com.svj.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    public CustomerService service;

    public CustomerController(CustomerService customerService){
        service= customerService;
    }

    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer){
        return service.saveCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers(){
        return service.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Integer id){
        return service.getCustomerById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteCustomerById(@PathVariable Integer id){
        return service.deleteCustomerById(id);
    }

    @PutMapping("/{id}")
    public Customer updateCustomerById(@PathVariable Integer id, @RequestBody Customer customer){
        return service.updateCustomerById(id,customer);
    }
}
