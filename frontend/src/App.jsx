import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CheckoutPage from "./pages/CheckoutPage.jsx";
import CheckoutCompletePage from "./pages/CheckoutCompletePage.jsx";

const App = () => {
    return (
        <div className="App">
            <Router>
                <Routes>
                    <Route path="/checkout" element={<CheckoutPage />} />
                    <Route path="/complete" element={<CheckoutCompletePage />} />
                </Routes>
            </Router>
        </div>
    );
};

export default App;
