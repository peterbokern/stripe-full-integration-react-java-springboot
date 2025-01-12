import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const CheckoutCompletePage = () => {
    const navigate = useNavigate();
    const [succeeded, setSucceeded] = useState(false);

    useEffect(() => {
        const paymentStatus = new URLSearchParams(window.location.search).get("redirect_status");

        //in future sava paymentdetails in database, then you can show meaningful message
        if (paymentStatus === "succeeded") {
            setSucceeded(true);
            navigate("/complete", { replace: true });
        } else {
            navigate("/complete", { replace: true });
        }
    }, [navigate]);

    return (
        <div>
            {succeeded ? <p>Betaling Gelukt</p> : <p>Betaling niet gelukt!...</p>}
        </div>
    );
};

export default CheckoutCompletePage;
