import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        try {

            System.out.println("=== PRODUCT CATALOG SETUP ===");
            Product p1 = new Product("Laptop", 50000, 5, "Electronics");
            Product p2 = new Product("Phone",  30000, 2, "Electronics");
            Product p3 = new Product("Desk",   15000, 8, "Furniture");


            System.out.println("\nElectronics category:");
            for (Product p : Product.getByCategory("Electronics")) {
                System.out.println("  " + p);
            }


            Product.removeFromCatalog("Furniture", "Desk");


            Product.printCatalog();


            System.out.println("\n=== CUSTOMER & PREFERRED CATEGORIES (Set) ===");
            Customer customer = new Customer("Alice", 100000);


            customer.addPreferredCategory("Electronics");
            customer.addPreferredCategory("Books");
            customer.addPreferredCategory("Electronics");


            System.out.println("Prefers Electronics? " + customer.prefersCategory("Electronics"));
            System.out.println("Prefers Clothing?    " + customer.prefersCategory("Clothing"));


            customer.removePreferredCategory("Books");
            customer.printPreferredCategories();


            System.out.println("\n=== CART (List) ===");
            Cart cart = new Cart();


            cart.addProduct(p1, 1);
            cart.addProduct(p2, 2);


            double total = cart.calculateTotal();
            System.out.println("Cart total: RWF " + total);


            cart.removeProduct("Phone");
            System.out.println("Cart total after removal: RWF " + cart.calculateTotal());


            cart.addProduct(p2, 1);
            double finalTotal = cart.calculateTotal();
            System.out.println("Final cart total: RWF " + finalTotal);

            cart.checkout();


            System.out.println("\n=== PAYMENT ===");
            Payment.processPayment(customer, finalTotal);
            System.out.println("Remaining balance: RWF " + customer.getBalance());


            System.out.println("\n=== ORDER HISTORY (Map<String, List<Double>>) ===");


            customer.recordOrder("ORD-001", Arrays.asList(50000.0, 30000.0));
            customer.recordOrder("ORD-002", List.of(15000.0));


            System.out.println("Items in ORD-001: " + customer.getOrder("ORD-001"));
            System.out.println("Items in ORD-002: " + customer.getOrder("ORD-002"));


            customer.removeOrder("ORD-002");


            customer.printOrderHistory();

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

        System.out.println("\nSystem continues running...");
    }
}
