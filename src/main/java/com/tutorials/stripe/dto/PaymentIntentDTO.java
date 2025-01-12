package com.tutorials.stripe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentIntentDTO {
    @JsonProperty("items")
    private PaymentRequestDTO[] items;

    @Getter
    public static class PaymentRequestDTO {
        @JsonProperty("id")
        String id;

        @JsonProperty("amount")
        Long amount;

        @Override
        public String toString() {
            return "PaymentRequestDTO{" +
                    "id='" + id + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }
}