public class Main {
    public static void main(String[] args) {

        try {
            Product p1 = new Product("Laptop", 50000, 5);
            Product p2 = new Product("Phone", 30000, 2);

            Customer customer = new Customer("Alice", 60000);

            Cart cart = new Cart();
            cart.addProduct(p1, 1);
            cart.addProduct(p2, 2); // may throw OutOfStock later

            double total = cart.calculateTotal();
            System.out.println("Total: RWF " + total);

            cart.checkout();

            Payment.processPayment(customer, total);

        } catch (OutOfStockException e) {
            System.out.println("Stock Error: " + e.getMessage());

        } catch (InvalidOrderException e) {
            System.out.println("Order Error: " + e.getMessage());

        } catch (CustomerException e) {
            System.out.println("Customer Error: " + e.getMessage());

        } catch (InvalidPaymentException e) {
            System.out.println("Payment Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Input: " + e.getMessage());
        }

        System.out.println("System continues running...");
    }
}
