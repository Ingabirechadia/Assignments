class Payment {

    public static void processPayment(Customer customer, double amount) {

        if (customer == null) {
            throw new InvalidPaymentException("Customer cannot be null");
        }

        if (amount <= 0) {
            throw new InvalidPaymentException("Invalid payment amount");
        }

        customer.deductBalance(amount);

        System.out.println("Payment successful: RWF " + amount);
    }
}
