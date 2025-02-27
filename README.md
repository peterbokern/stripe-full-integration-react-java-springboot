
# Comprehensive Stripe Payment Integration Setup Guide

This README provides a complete and detailed tutorial for setting up a Stripe payment integration project using React for the frontend and Spring Boot for the backend. It includes an in-depth explanation of every step, from initializing Stripe to securely handling payments and managing webhook notifications. Additionally, advanced testing tools like Stripe CLI and Ngrok are covered for local testing and debugging.


---

## **Project Overview**

This project demonstrates how to integrate Stripe's payment processing system into a full-stack web application. Stripe provides tools to handle payment processing seamlessly, including collecting payment details, processing transactions, and managing real-time updates.

### **Components of the Project**
- **Frontend:** React application using Stripe.js and Stripe Elements for securely collecting payment information.
- **Backend:** Spring Boot application for managing PaymentIntent objects, communicating with Stripe, and processing webhooks.
- **Database:** PostgreSQL for storing user, order, and payment information.
- **Stripe API:** Manages payment workflows, from creating PaymentIntents to handling confirmations.

This integration leverages Stripe’s secure payment infrastructure, ensuring PCI DSS compliance and a seamless user experience.

---

# Project Name

## Overview
This project is a full-stack application built with a Spring Boot backend and a Vite-powered React frontend. It integrates Stripe for payment processing and is designed for quick setup and deployment in a local development environment.

---

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Setup Instructions](#setup-instructions)
    - [Clone the Repository](#clone-the-repository)
    - [Configure the Backend](#configure-the-backend)
    - [Configure the Frontend](#configure-the-frontend)
    - [Install Dependencies](#install-dependencies)
    - [Run the Application](#run-the-application)
3. [Stripe Configuration](#stripe-configuration)
4. [Webhook Setup with Ngrok](#webhook-setup-with-ngrok)
5. [Running the Full Project](#running-the-full-project)
6. [Troubleshooting](#troubleshooting)

---

## Prerequisites
Before you begin, ensure you have the following installed:
- **Java 17 or higher**
- **Maven** (for backend dependencies)
- **Node.js** (v14 or higher) and **npm** (for frontend dependencies)
- **PostgreSQL** database
- **Ngrok** (for local webhook testing)
- Stripe account with API keys

---

## Setup Instructions

### Clone the Repository
1. Clone the repository and navigate to the project directory:
    ```bash
    git clone <repository-url>
    cd <repository-name>
    ```

---

### Configure the Backend
1. Navigate to the `backend` folder and locate the `application.properties` file.
2. Update the configuration with your database and Stripe details:

   Example `application.properties`:
    ```properties
    # Database Configuration
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
    spring.datasource.username=your_database_user
    spring.datasource.password=your_password

    # Hibernate Configuration
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.properties.hibernate.format_sql=true

    # Server Configuration
    server.address=127.0.0.1
    server.port=4242

    # Stripe Configuration
    stripe.api.key=sk_test_your_secret_key
    stripe.webhook.secret=whsec_your_webhook_secret
    ```

---

### Configure the Frontend
1. Navigate to the `frontend` folder.
2. Create a `.env` file by copying the `.env.example` file:
    ```bash
    cp .env.example .env
    ```
3. Update the `.env` file with the necessary variables:

   Example `.env`:
    ```env
    VITE_MODE=development
    VITE_STRIPE_PK=pk_test_your_publishable_key
    VITE_BACKEND_BASE_URL=http://127.0.0.1:4242
    ```

---

### Install Dependencies

#### Backend:
1. Navigate to the `backend` folder and build the project:
    ```bash
    ./mvnw clean install
    ```

#### Frontend:
1. Navigate to the `frontend` folder and install dependencies:
    ```bash
    cd frontend
    npm install
    ```

---

### Run the Application

#### Backend:
1. Start the backend server:
    ```bash
    ./mvnw spring-boot:run
    ```
   The backend will run on `http://127.0.0.1:4242`.

#### Frontend:
1. Start the frontend:
    ```bash
    cd frontend
    npm run dev
    ```
   The frontend will run on the localhost address displayed in the terminal. 
NOTE: GO to http://localhost:5173/checkout (change port to actual port)

---

## Stripe Configuration
1. Log in to your Stripe account and copy the API keys.
2. Add your **secret key** to the `application.properties` file:
    ```properties
    stripe.api.key=sk_test_your_secret_key
    ```
3. Add your **publishable key** to the `.env` file in the frontend:
    ```env
    VITE_STRIPE_PK=pk_test_your_publishable_key
    ```

---

## Webhook Setup with Ngrok
1. Install and start Ngrok to expose your local backend:
    ```bash
    ngrok http 4242
    ```
2. Copy the generated HTTPS URL and append `/webhook`.
   Example: `https://<ngrok-id>.ngrok.io/webhook`
3. Configure this URL in the **Webhooks** section of your Stripe Dashboard:
    - Add events:
        - `payment_intent.succeeded`
        - `payment_intent.payment_failed`

---

## Running the Full Project
1. Start the backend and frontend in separate terminals:

   **Terminal 1: Backend**
   ```bash
   ./mvnw spring-boot:run
   ```

   **Terminal 2: Frontend**
   ```bash
   cd frontend
   npm run dev
   ```

2. Verify that both the backend and frontend are running and accessible:
    - Backend: `http://127.0.0.1:4242`
    - Frontend: Address displayed in the terminal (e.g., `http://localhost:3000`).

---

## Troubleshooting

### Common Issues:
1. **Database Connection Errors**:
    - Verify your `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` in the `application.properties` file.
    - Ensure the PostgreSQL database is running and accessible.

2. **CORS Errors**:
    - Update the web configuration in the backend to allow requests from the frontend URL.
    - Use a Chrome extension to bypass CORS for localhost during development.

3. **Ngrok Issues**:
    - Ensure Ngrok is running and the HTTPS URL is correctly configured in Stripe Webhooks.

4. **Frontend Not Starting**:
    - Ensure all dependencies are installed with `npm install`.

---

### Congratulations!
Your project should now be running locally. Test the Stripe payment flows and other functionalities to ensure everything is working as expected.





## **How Stripe Payment Workflow Works**

### **Step 1: Frontend Initialization**
1. **Initialize Stripe.js** with a Publishable Key specific to your Stripe account.
2. Use Stripe.js to integrate prebuilt payment UI components like `CardElement`:
    - Collects sensitive user payment details securely.
    - Sends sensitive data directly to Stripe, bypassing the backend for PCI compliance.

---

### **Step 2: Frontend Sends Order Details to Backend**
- The frontend sends a `POST` request to an endpoint like `/create-payment-intent` with order details:
    - **Example Payload:**
      ```json
  {
  "items": [
  { "productId": 1, "amount": 1000 },
  { "productId": 2, "amount": 3000 }
  ]
  }
      ```

---

### **Step 3: Backend Creates a PaymentIntent**
1. The backend:
    - Calculates the total amount based on order details.
    - Communicates with Stripe’s API using the Secret Key to create a `PaymentIntent`.
2. Stripe returns a `PaymentIntent` object with a **Client Secret**.

---

### **Step 4: Backend Sends Client Secret to Frontend**
- The backend sends the `Client Secret` back to the frontend, enabling secure payment confirmation.

---

### **Step 5: Frontend Confirms Payment with Stripe**
1. The frontend uses the `Client Secret` and Stripe.js to confirm the payment:
    - User enters payment details in `CardElement`.
    - Stripe processes the payment.
2. Stripe responds with a status:
    - `succeeded`: Payment successful.
    - `requires_action`: Further user action required.
    - `requires_payment_method`: Payment failed; new details needed.

---

### **Step 6: Stripe Sends Webhook Notifications to Backend**
1. Stripe sends real-time webhook events to the backend for payment status updates.
2. The backend:
    - Verifies the webhook using the Webhook Secret.
    - Updates the database and triggers any required actions (e.g., email notifications).

---

## **Setting Up the Project**

### **Step 1: Prerequisites**
1. Stripe account with API keys.
2. PostgreSQL installed and running.
3. Java 17+ and Node.js with npm installed.
4. Install:
    - **Ngrok**: Exposes local endpoints to Stripe.
    - **Stripe CLI**: Simulates webhook events.

---

### **Step 2: Stripe Configuration**
1. Log in to the Stripe Dashboard and copy:
    - Publishable Key: For frontend initialization.
    - Secret Key: For backend authentication.
2. Add a webhook endpoint in Stripe:
    - Use `http://localhost:4242/webhook` as the placeholder URL.
    - Listen for events:
        - `payment_intent.succeeded`
        - `payment_intent.payment_failed`

---

### **Step 3: Frontend Setup**
1. **Create a React project**:
   ```bash
   vite create frontend
   ```
2. Install dependencies:
   ```bash
   npm install @stripe/stripe-js @stripe/react-stripe-js
   ```
3. Configure `.env.development`:
   ```
   VITE_MODE=development
   VITE_STRIPE_PK=pk_test_your_publishable_key
   VITE_BACKEND_BASE_URL=http://localhost:4242
   ```
4. Build components:
    - `Checkout` component: Fetches Client Secret.
    - `PaymentForm` component: Uses Stripe Elements for payment details.

---

### **Step 4: Backend Setup**
1. Generate a Spring Boot project with:
    - Spring Web, Spring Data JPA, PostgreSQL Driver.
2. Configure `application.properties`:
   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   stripe.api.key=sk_test_your_secret_key
   stripe.webhook.secret=whsec_your_webhook_secret
   ```
3. Implement:
    - A controller to handle `/create-payment-intent`.
    - A service to interact with Stripe’s API.

---

### **Step 5: Webhook Setup**
1. Install Ngrok:
   ```bash
   npm install -g ngrok
   ```
2. Start Ngrok:
   ```bash
   ngrok http 4242
   ```
3. Update the Stripe webhook URL with the Ngrok-generated HTTPS URL.
4. Test with Stripe CLI:
   ```bash
   stripe trigger payment_intent.succeeded
   ```

---

### **Step 6: Testing**
- Use test card numbers:
    - Successful payment: `4242 4242 4242 4242`
    - Declined payment: `4000 0000 0000 9995`
- Check logs for proper request/response handling.

---

### **Step 7: Deployment**
1. **Frontend**:
    - Deploy to Vercel or Netlify.
2. **Backend**:
    - Deploy to AWS or Heroku.
3. Update the webhook URL in Stripe Dashboard to the production backend.

---

## **Conclusion**
This guide provides step-by-step instructions for integrating Stripe into a full-stack web application. For advanced features, consult the [Stripe Documentation](https://stripe.com/docs). 
