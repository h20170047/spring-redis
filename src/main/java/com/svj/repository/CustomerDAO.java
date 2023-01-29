package com.svj.repository;

import com.svj.hash.Customer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDAO {
    public static final String HASH_KEY = "Customer";
    private RedisTemplate template;

    public CustomerDAO(RedisTemplate template){
        this.template= template;
    }

    public Customer addCustomer(Customer customer){
        template.opsForHash().put(HASH_KEY,customer.getId(),customer);
        return customer;
    }

    public List<Customer> getAllCustomers(){
        return template.opsForHash().values(HASH_KEY);
    }

    public Customer getCustomerById(int id){
        return (Customer) template.opsForHash().get(HASH_KEY, id);
    }

    public String deleteCustomerById(int id){
        Long deletedEntry = template.opsForHash().delete(HASH_KEY, id);
        return String.format("Customer %s has been removed with return code- %s", id, deletedEntry);
    }

    public Customer updateCustomerById(int id, Customer customer){
        Customer existingCustomer = (Customer) template.opsForHash().get(HASH_KEY, id);
        if(existingCustomer!= null){
            existingCustomer.setDob(customer.getDob());
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhone(customer.getPhone());
            template.opsForHash().put(HASH_KEY, id, existingCustomer);
            return existingCustomer;
        }else{
            throw new RuntimeException("Customer not found!");
        }
    }
}
