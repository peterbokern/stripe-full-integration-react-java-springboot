import {useState} from "react";
import {
    PaymentElement,
    useStripe,
    useElements
} from "@stripe/react-stripe-js";

const CheckoutForm = () => {
    const stripe = useStripe();
    const elements = useElements();

    const [message, setMessage] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!stripe || !elements) {
            // Stripe.js hasn't yet loaded.
            // Make sure to disable form submission until Stripe.js has loaded.
            return;
        }

        setIsLoading(true);

        //When the user submits payment details, it communicates with stripe using the clientsecret (in elements)
        //and waits for payment confirmation
        const {error} = await stripe.confirmPayment({
            elements,
            confirmParams: {
                // On success, reroute to:
                return_url: "http://localhost:5173/complete",
            },
        });

        // This point will only be reached if there is an immediate error when
        // confirming the payment. Otherwise, your customer will be redirected to
        // your `return_url`. For some payment methods like iDEAL, your customer will
        // be redirected to an intermediate site first to authorize the payment, then
        // redirected to the `return_url`.
        if (error.type === "card_error" || error.type === "validation_error") {
            setMessage(error.message);
        } else {
            setMessage("An unexpected error occurred.");
        }

        setIsLoading(false);
    };

    const paymentElementOptions = {
        layout: "accordion"
    }

    return (
        <>
            <form id="payment-form" onSubmit={handleSubmit}>

                <PaymentElement id="payment-element" options={paymentElementOptions}/>
                <button disabled={isLoading || !stripe || !elements} id="submit">
                    <span id="button-text">
                      {isLoading ? <div className="spinner" id="spinner"></div> : "Pay now"}
                    </span>
                </button>

                {/* Show any error or success messages */}
                {message && <div id="payment-message">{message}</div>}
            </form>

        </>
    );
}

export default CheckoutForm;