import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        Customer customer = new Customer("Chadia");

        ArrayList<Product> products = new ArrayList<>();
        ArrayList<Order> cart = new ArrayList<>();


        products.add(new Product("Laptop", 600000));
        products.add(new Product("Phone", 250000));
        products.add(new Product("Headphones", 20000));

        int choice;

        do {
            System.out.println("\n--- Shopping System ---");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            choice = input.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("\n Available Products:");
                    for (int i = 0; i < products.size(); i++) {
                        System.out.print(i + ". ");
                        products.get(i).displayProduct();
                    }
                    break;

                case 2:
                    System.out.print("Enter product index: ");
                    int index = input.nextInt();

                    if (index >= 0 && index < products.size()) {
                        System.out.print("Enter quantity: ");
                        int qty = input.nextInt();

                        cart.add(new Order(products.get(index), qty));
                        System.out.println("Added to cart!");
                    } else {
                        System.out.println("Invalid product.");
                    }
                    break;

                case 3:
                    System.out.println("\nYour Cart:");
                    for (Order o : cart) {
                        o.displayOrder();
                    }
                    break;

                case 4:
                    double total = 0;
                    System.out.println("\nCheckout:");

                    for (Order o : cart) {
                        o.displayOrder();
                        total += o.getTotal();
                    }

                    System.out.println("Total Amount: FRW" + total);
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);

        input.close();
    }
}

