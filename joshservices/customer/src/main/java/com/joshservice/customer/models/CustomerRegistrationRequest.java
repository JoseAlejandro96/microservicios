package com.joshservice.customer.models;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
