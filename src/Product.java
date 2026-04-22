import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class Product {
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

    public String getName()     { return name; }
    public double getPrice()    { return price; }
    public int getStock()       { return stock; }
    public String getCategory() { return category; }

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
        return productCatalog.getOrDefault(category, List.of());
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
        for (Map.Entry<String, List<Product>> entry : productCatalog.entrySet()) {
            System.out.println("  Category: " + entry.getKey());
            for (Product p : entry.getValue()) {
                System.out.println("    - " + p.getName() + " | RWF " + p.getPrice() + " | Stock: " + p.getStock());
            }
        }
    }

    @Override
    public String toString() {
        return name + " [" + category + "] @ RWF " + price;
    }
}

