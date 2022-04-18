package com.joshservice.customer.service;

import com.joshservice.amqp.RabbitMQMessageProducer;
import com.joshservice.clients.fraud.FraudCheckResponse;
import com.joshservice.clients.fraud.FraudClient;
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
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

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
        log.info(customer.toString());
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to the system...",
                        customer.getFirstName())
        );

        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
            );
    }
}
