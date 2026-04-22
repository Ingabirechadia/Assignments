import java.util.ArrayList;
import java.util.List;


class Cart {


    private List<CartItem> items = new ArrayList<>();


    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new InvalidOrderException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new InvalidOrderException("Quantity must be greater than zero");
        }
        items.add(new CartItem(product, quantity));
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
    }


    public List<CartItem> getItems() {
        return List.copyOf(items);
    }
}
