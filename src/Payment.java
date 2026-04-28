import java.io.*;
import java.util.Date;

class Payment {

    private static final String TRANSACTIONS_FILE = "transactions.txt";

    public static void processPayment(Customer customer, double amount) {
        if (customer == null) {
            throw new InvalidPaymentException("Customer cannot be null");
        }
        if (amount <= 0) {
            throw new InvalidPaymentException("Invalid payment amount");
        }

        customer.deductBalance(amount);
        System.out.println("Payment successful: RWF " + amount);


        logTransaction(customer.getName(), amount);
    }

    private static void logTransaction(String customerName, double amount) {
        try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.printf("%s | Customer: %s | Amount: RWF %.2f%n",
                    new Date(), customerName, amount);
            System.out.println("✓ Transaction logged to " + TRANSACTIONS_FILE);
        } catch (IOException e) {
            System.err.println("Error logging transaction: " + e.getMessage());
        }
    }
}