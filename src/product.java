class Product {
    private String name;
    private double price;
    private int stock;

    public Product(String name, double price, int stock) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");

        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void reduceStock(int quantity) {
        if (quantity > stock) {
            throw new OutOfStockException("Not enough stock for " + name);
        }
        stock -= quantity;
    }
}