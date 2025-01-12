

import { useState, useEffect } from "react";
import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";
import CheckoutForm from "../components/CheckoutForm.jsx";
import "./CheckoutPage.css";
import axios from "axios";

// Make sure to call loadStripe outside of a component’s render to avoid
// recreating the Stripe object on every render.
// This is your test publishable API key.
const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PK);

export default function CheckoutPage() {
    const [clientSecret, setClientSecret] = useState("");
    const baseUrl = import.meta.env.VITE_BACKEND_BASE_URL;


    //API call to backend with order details to create a payment session
    useEffect(() => {
        axios.post(`${baseUrl}/create-payment-intent`, {
            items: [{ id: "xl-tshirt", amount: 1000 }],
        })
            //backend sends api request to stripe and returns a clientsecret, a unique key linked to the payment
            //intent
            .then((response) => {
                const data = response.data;
                setClientSecret(data.clientSecret);
            })
            .catch((error) => {
                console.error("Error creating PaymentIntent:", error);
            });
    }, []);

    const appearance = {
        theme: "stripe",
    };

    const loader = "auto";

    return (
        //The clientsecret is required to load the stripe elements ans setup a secure connection. Elements initalizes the stripe elements form stripe,
        //and is a context provider for its inner components
        <div>
            {clientSecret ? (
                <Elements options={{ clientSecret, appearance, loader }} stripe={stripePromise}>
                    <CheckoutForm />
                </Elements>
            ) : (
                <p>Loading payment details...</p>
            )}
        </div>
    );
}
