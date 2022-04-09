package com.joshservice.customer.service;

import com.joshservice.customer.entities.Customer;
import com.joshservice.customer.models.CustomerRegistrationRequest;
import com.joshservice.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepository customerRepository) {

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken

        // store customer in db
        customerRepository.save(customer);
    }
}
