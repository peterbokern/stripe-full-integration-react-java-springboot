package com.tutorials.stripe.controller;

import com.tutorials.stripe.service.StripeWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    private final StripeWebhookService webservice;

    @Autowired
    public StripeWebhookController(StripeWebhookService webservice) {
        this.webservice = webservice;
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {return webservice.handleStripeWebhook(payload, sigHeader);}
}

