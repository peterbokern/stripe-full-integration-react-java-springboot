package com.tutorials.stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.tutorials.stripe.dto.PaymentIntentDTO;
import com.tutorials.stripe.dto.PaymentIntentResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentIntentService {

    @Value("${stripe.api.key}")
    private String apiKey;

    public ResponseEntity<?> createPaymentIntent(PaymentIntentDTO request) {
        try {

            Stripe.apiKey = apiKey;

            // Create the PaymentIntent parameters
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(1000L)
                    .setCurrency("eur")
                    .setDescription("Thanks for your purchase")
                    .setReceiptEmail("peterbokern@gmail.com")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            // Create the PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Return the client secret to the frontend
            PaymentIntentResponseDTO paymentResponse = new PaymentIntentResponseDTO(paymentIntent.getClientSecret());

            System.out.println("CHECKOUT-CONTROLLER: " + "PaymentIntent created with id: " + paymentIntent.getId() + "and client secret:" + paymentIntent.getClientSecret());
            System.out.println("CHECKOUT-CONTROLLER: " + "Returning paymentresponse: " + paymentResponse.toString());
            return ResponseEntity.ok(paymentResponse); //paymentresponse is {clientsecret:xxxxxxxx}
        } catch (StripeException e) {
            System.out.println("CHECKOUT-CONTROLLER: Stripe exception: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to create PaymentIntent: " + e.getMessage());
        }
    }

    // Replace this with your actual logic to calculate the order amount
    private long calculateOrderAmount(Map<String, PaymentIntentDTO> items) {
        // For now, return a fixed amount for simplicity (e.g., 10 EUR in cents)
        return 1000L;
    }
}

