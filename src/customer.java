class Customer extends User {

    public Customer(String name) {
        super(name);
    }

    @Override
    public void displayInfo() {
        System.out.println("Customer: " + getName());
    }
}
