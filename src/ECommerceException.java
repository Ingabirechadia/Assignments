class ECommerceException extends RuntimeException {
    public ECommerceException(String message) {
        super(message);
    }
}

class InvalidOrderException extends ECommerceException {
    public InvalidOrderException(String message) {
        super(message);
    }
}

class OutOfStockException extends ECommerceException {
    public OutOfStockException(String message) {
        super(message);
    }
}

class InvalidPaymentException extends ECommerceException {
    public InvalidPaymentException(String message) {
        super(message);
    }
}

class CustomerException extends ECommerceException {
    public CustomerException(String message) {
        super(message);
    }
}