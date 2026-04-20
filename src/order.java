class Order {
    private Product product;
    private int quantity;

    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }

    public void displayOrder() {
        System.out.println(product.getName() + " x" + quantity + " = RWF" + getTotal());
    }
}