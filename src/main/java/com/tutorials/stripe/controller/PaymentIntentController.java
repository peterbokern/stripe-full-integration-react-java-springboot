
package com.tutorials.stripe.controller;
import com.tutorials.stripe.dto.PaymentIntentDTO;
import com.tutorials.stripe.service.PaymentIntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
/*@RequestMapping("/api")*/
@CrossOrigin(origins = "http://localhost:5174") // Allow frontend origin
public class PaymentIntentController {

    private final PaymentIntentService paymentIntentService;

    @Autowired
    public PaymentIntentController(PaymentIntentService paymentIntentService) {this.paymentIntentService = paymentIntentService;}

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentIntentDTO request) {
        return paymentIntentService.createPaymentIntent(request);
    }
}
