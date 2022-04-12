package com.joshservice.customer.service;

import com.joshservice.clients.fraud.FraudCheckResponse;
import com.joshservice.clients.fraud.FraudClient;
import com.joshservice.clients.notification.NotificationClient;
import com.joshservice.clients.notification.NotificationRequest;
import com.joshservice.customer.entities.Customer;
import com.joshservice.customer.models.CustomerRegistrationRequest;
import com.joshservice.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService{

    private final CustomerRepository customerRepository;
    private final NotificationClient notificationClient;
    private final FraudClient fraudClient;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken
        // store customer in db
        customerRepository.saveAndFlush(customer);
        // check if fraudster

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }
        // send notification
        // todo: make it async. i.e add to queue
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to the system...",
                                customer.getFirstName())
                )
        );
    }
}
