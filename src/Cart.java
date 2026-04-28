import java.io.*;
import java.util.*;

class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<CartItem> items = new ArrayList<>();
    private String customerName;

    public Cart() {
        this.customerName = null;
    }

    public Cart(String customerName) {
        this.customerName = customerName;
        loadFromFile();
    }

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new InvalidOrderException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new InvalidOrderException("Quantity must be greater than zero");
        }

        for (CartItem item : items) {
            if (item.getProduct().getName().equalsIgnoreCase(product.getName())) {
                item.setQuantity(item.getQuantity() + quantity);
                saveToFile();
                System.out.println("Updated in cart: " + product.getName() + " x" + item.getQuantity());
                return;
            }
        }

        items.add(new CartItem(product, quantity));
        saveToFile();
        System.out.println("Added to cart: " + product.getName() + " x" + quantity);
    }

    public void removeProduct(String productName) {
        CartItem toRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getName().equalsIgnoreCase(productName)) {
                toRemove = item;
                break;
            }
        }
        if (toRemove != null) {
            items.remove(toRemove);
            saveToFile();
            System.out.println("Removed from cart: " + productName);
        } else {
            System.out.println("Product not found in cart: " + productName);
        }
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public void checkout() {
        for (CartItem item : items) {
            item.getProduct().reduceStock(item.getQuantity());
        }
        items.clear();
        saveToFile();
        System.out.println("Checkout complete. Cart emptied.");
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.size();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    public void printCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("\n--- YOUR CART ---");
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            System.out.printf("  %d. %s x%d = RWF %.2f%n",
                    i+1, item.getProduct().getName(), item.getQuantity(),
                    item.getProduct().getPrice() * item.getQuantity());
        }
        System.out.printf("Total: RWF %.2f%n", calculateTotal());
    }


    private static final String CARTS_FILE = "carts.txt";

    private void saveToFile() {
        if (customerName == null || customerName.isEmpty()) return;

        Map<String, List<CartItem>> allCarts = loadAllCarts();
        allCarts.put(customerName, items);

        try (PrintWriter writer = new PrintWriter(new FileWriter(CARTS_FILE))) {
            writer.println("=== SHOPPING CARTS ===");
            writer.println("Customer|Product|Quantity|UnitPrice|Subtotal");
            writer.println("--------|-------|--------|---------|--------");

            for (Map.Entry<String, List<CartItem>> entry : allCarts.entrySet()) {
                for (CartItem item : entry.getValue()) {
                    writer.printf("%s|%s|%d|%.2f|%.2f%n",
                            entry.getKey(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getProduct().getPrice(),
                            item.getProduct().getPrice() * item.getQuantity()
                    );
                }
            }
            writer.println("=== END OF CARTS ===");
        } catch (IOException e) {

        }
    }

    private Map<String, List<CartItem>> loadAllCarts() {
        Map<String, List<CartItem>> allCarts = new HashMap<>();
        File file = new File(CARTS_FILE);
        if (!file.exists()) return allCarts;

        try (BufferedReader reader = new BufferedReader(new FileReader(CARTS_FILE))) {
            String line;
            boolean readingData = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Customer|Product|Quantity|UnitPrice|Subtotal")) {
                    readingData = true;
                    continue;
                }
                if (readingData && line.contains("===")) {
                    break;
                }
                if (readingData && !line.isEmpty() && line.contains("|")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        String custName = parts[0];
                        String productName = parts[1];
                        int quantity = Integer.parseInt(parts[2]);

                        Product product = Product.findProductByName(productName);
                        if (product != null) {
                            CartItem item = new CartItem(product, quantity);
                            allCarts.computeIfAbsent(custName, k -> new ArrayList<>()).add(item);
                        }
                    }
                }
            }
        } catch (IOException e) {

        }
        return allCarts;
    }

    private void loadFromFile() {
        if (customerName == null || customerName.isEmpty()) return;
        Map<String, List<CartItem>> allCarts = loadAllCarts();
        if (allCarts.containsKey(customerName)) {
            items.clear();
            items.addAll(allCarts.get(customerName));
        }
    }
}