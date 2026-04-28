import java.io.*;
import java.util.*;

class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private double price;
    private int stock;
    private String category;
    private static Map<String, List<Product>> productCatalog = new HashMap<>();


    public Product(String name, double price, int stock, String category) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");

        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;

        registerInCatalog(this);
    }

    public Product(String name, double price, int stock) {
        this(name, price, stock, "General");
    }


    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getCategory() { return category; }


    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        this.stock = stock;
    }

    public void reduceStock(int quantity) {
        if (quantity > stock) {
            throw new OutOfStockException("Not enough stock for " + name);
        }
        stock -= quantity;
    }

    private static void registerInCatalog(Product product) {
        productCatalog.putIfAbsent(product.category, new ArrayList<>());
        productCatalog.get(product.category).add(product);
        System.out.println("Registered in catalog: " + product.name + " [" + product.category + "]");
    }

    public static List<Product> getByCategory(String category) {
        return productCatalog.getOrDefault(category, new ArrayList<>());
    }

    public static List<Product> getAllProducts() {
        List<Product> all = new ArrayList<>();
        for (List<Product> products : productCatalog.values()) {
            all.addAll(products);
        }
        return all;
    }

    public static Product findProductByName(String name) {
        for (List<Product> products : productCatalog.values()) {
            for (Product p : products) {
                if (p.getName().equalsIgnoreCase(name)) {
                    return p;
                }
            }
        }
        return null;
    }

    public static void removeFromCatalog(String category, String productName) {
        List<Product> products = productCatalog.get(category);
        if (products != null) {
            products.removeIf(p -> p.getName().equalsIgnoreCase(productName));
            System.out.println("Removed from catalog: " + productName);
        }
    }

    public static void printCatalog() {
        System.out.println("\n--- Product Catalog ---");
        if (productCatalog.isEmpty()) {
            System.out.println("  No products available.");
        } else {
            for (Map.Entry<String, List<Product>> entry : productCatalog.entrySet()) {
                System.out.println("  Category: " + entry.getKey());
                for (Product p : entry.getValue()) {
                    System.out.println("    - " + p.getName() + " | RWF " + p.getPrice() + " | Stock: " + p.getStock());
                }
            }
        }
    }


    private static final String PRODUCTS_FILE = "products.txt";

    public static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCTS_FILE))) {
            writer.println("=== PRODUCT CATALOG ===");
            writer.println("Name|Category|Price|Stock");
            writer.println("---|--------|-----|-----");

            for (Map.Entry<String, List<Product>> entry : productCatalog.entrySet()) {
                for (Product p : entry.getValue()) {
                    writer.printf("%s|%s|%.2f|%d%n",
                            p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                }
            }
            writer.println("=== END OF CATALOG ===");
            System.out.println("✓ Products saved to " + PRODUCTS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    public static void loadFromFile() {
        File file = new File(PRODUCTS_FILE);
        if (!file.exists()) {
            System.out.println("No saved products found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            productCatalog.clear();
            String line;
            boolean readingData = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Name|Category|Price|Stock")) {
                    readingData = true;
                    continue;
                }
                if (readingData && line.contains("===")) {
                    break;
                }
                if (readingData && !line.isEmpty() && line.contains("|") && !line.startsWith("-")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        String name = parts[0];
                        String category = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        int stock = Integer.parseInt(parts[3]);
                        new Product(name, price, stock, category);
                    }
                }
            }
            System.out.println("✓ Products loaded from " + PRODUCTS_FILE);
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return name + " [" + category + "] @ RWF " + price + " (Stock: " + stock + ")";
    }
}