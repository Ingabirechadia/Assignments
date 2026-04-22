import java.util.ArrayList;

class Cart {
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Integer> quantities = new ArrayList<>();

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new InvalidOrderException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new InvalidOrderException("Quantity must be greater than zero");
        }

        products.add(product);
        quantities.add(quantity);
    }

    public double calculateTotal() {
        double total = 0;

        for (int i = 0; i < products.size(); i++) {
            total += products.get(i).getPrice() * quantities.get(i);
        }

        return total;
    }

    public void checkout() {
        for (int i = 0; i < products.size(); i++) {
            products.get(i).reduceStock(quantities.get(i));
        }
    }
}
