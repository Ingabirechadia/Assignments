class Customer {
    private String name;
    private double balance;

    public Customer(String name, double balance) {
        if (name == null || name.isEmpty()) {
            throw new CustomerException("Customer name cannot be empty");
        }
        if (balance < 0) {
            throw new CustomerException("Balance cannot be negative");
        }

        this.name = name;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deductBalance(double amount) {
        if (amount > balance) {
            throw new CustomerException("Insufficient balance");
        }
        balance -= amount;
    }
}