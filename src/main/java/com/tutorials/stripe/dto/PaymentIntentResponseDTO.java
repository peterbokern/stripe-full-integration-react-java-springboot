package com.tutorials.stripe.dto;

//record class is immutable and clientSec
public record PaymentIntentResponseDTO(String clientSecret) {

    @Override
    public String toString() {
        return "PaymentIntentResponse{" +
                "clientSecret='" + clientSecret + '\'' +
                '}';
    }
}