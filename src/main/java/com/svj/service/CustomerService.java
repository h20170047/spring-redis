package com.svj.service;

import com.svj.hash.Customer;
import com.svj.repository.CustomerDAO;
import com.svj.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CustomerService {
    public static final String CACHE_NAME = "customers";
    public static final String ALL_CUSTOMERS = "allCustomers";
    private CustomerDAO dao;
    private CustomerRepository repository;
    private CacheManager cacheManager;

    public CustomerService(CustomerDAO customerDAO, CustomerRepository customerRepository, CacheManager cacheManager){
        dao= customerDAO;
        this.repository= customerRepository;
        this.cacheManager= cacheManager;
    }

    @CachePut(value = CACHE_NAME, key = "#customer.id")
    public Customer saveCustomer(Customer customer){
//        return dao.addCustomer(customer);
        log.info("CustomerService: saveCustomer connecting to DB");
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Cache.ValueWrapper valueWrapper = cache.get(ALL_CUSTOMERS);
        if(valueWrapper!= null){
            List<Customer> allCustomers= (List<Customer>) valueWrapper.get();
            allCustomers.add(customer);
            cache.put(ALL_CUSTOMERS, allCustomers);
        }
        return repository.save(customer);
    }

    @Cacheable(value = CACHE_NAME, key = "#root.target.ALL_CUSTOMERS")
    public List<Customer> getAllCustomers(){
//        return dao.getAllCustomers();
        log.info("CustomerService: getAllCustomers connecting to DB");
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id", value = CACHE_NAME)
    public Customer getCustomerById(int id){
//        return dao.getCustomerById(id);
        log.info("CustomerService: getCustomerById connecting to DB");
        return repository.findById(id).orElseThrow(()-> new RuntimeException("Customer not found with given ID"));
    }

    @CacheEvict(key = "#id", value = CACHE_NAME)
    public String deleteCustomerById(int id){
//        return dao.deleteCustomerById(id);
        log.info("CustomerService: deleteCustomerById connecting to DB");
        repository.deleteById(id);
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Cache.ValueWrapper valueWrapper = cache.get(ALL_CUSTOMERS);
        if(valueWrapper!= null){
            List<Customer> allCustomers= (List<Customer>) valueWrapper.get();
            allCustomers= allCustomers.stream().filter(cust-> cust.getId()!= id).collect(Collectors.toList());
            cache.put(ALL_CUSTOMERS, allCustomers);
        }
        return "Customer removed!";
    }

    @CachePut(key = "#id", value = CACHE_NAME)
    public Customer updateCustomerById(int id, Customer customer){
//        return dao.updateCustomerById(id,customer);
        log.info("CustomerService: updateCustomerById connecting to DB");
        Customer existingCustomer = repository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with given ID"));
        existingCustomer.setDob(customer.getDob());
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Cache.ValueWrapper valueWrapper = cache.get(ALL_CUSTOMERS);
        if(valueWrapper!= null){
            List<Customer> allCustomers= (List<Customer>) valueWrapper.get();
            allCustomers= allCustomers.stream().filter(cust-> cust.getId()!= id).collect(Collectors.toList());
            allCustomers.add(existingCustomer);
            cache.put(ALL_CUSTOMERS, allCustomers);
        }
        return repository.save(existingCustomer);
    }

}
