package com.tutorials.stripe.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.tutorials.stripe.utils.UnixDateConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StripeWebhookService {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookService.class);

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public ResponseEntity<?> handleStripeWebhook(String payload, String sigHeader) {
        Event event;

        // Verify the signature and construct the event object
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            logger.error("Signature verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Webhook error: " + e.getMessage());
        }

        logger.info("Event Type: {}", event.getType());

        // Deserialize the event object
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (!dataObjectDeserializer.getObject().isPresent()) {
            logger.error("Deserialization failed. Raw JSON: {}", dataObjectDeserializer.getRawJson());
            return ResponseEntity.badRequest().body("Deserialization error");
        }

        StripeObject stripeObject = dataObjectDeserializer.getObject().get();

        // Handle event types
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handlePaymentIntentSucceeded((PaymentIntent) stripeObject);
            case "payment_intent.payment_failed" -> handlePaymentIntentFailed((PaymentIntent) stripeObject);
            default -> logger.warn("Unhandled event type: {}", event.getType());
        }

        return ResponseEntity.ok("Webhook processed successfully");
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        // Extract essential fields
        String paymentIntentId = paymentIntent.getId();
        long amount = paymentIntent.getAmount();
        String currency = paymentIntent.getCurrency();
        String customerId = paymentIntent.getCustomer();
        String paymentMethodId = paymentIntent.getPaymentMethod();
        String status = paymentIntent.getStatus();
        long createdTimestamp = paymentIntent.getCreated();

        // Log payment details
        logger.info("PaymentIntent succeeded: ID={}, Amount={}, Currency={}, Customer={}, PaymentMethod={}, Status={}, Created={}",
                paymentIntentId, amount, currency, customerId, paymentMethodId, status, UnixDateConverter.convert(createdTimestamp));
    }

    private void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
        String paymentIntentId = paymentIntent.getId();
        String errorMessage = paymentIntent.getLastPaymentError() != null
                ? paymentIntent.getLastPaymentError().getMessage()
                : "Unknown error";

        logger.warn("PaymentIntent failed: ID={}, Error={}", paymentIntentId, errorMessage);
    }

}
