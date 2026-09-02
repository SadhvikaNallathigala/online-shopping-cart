package com.example.shoppingcart.exception;

public class CustomExceptions {

    public static class InvalidCouponException
            extends RuntimeException {
        public InvalidCouponException(String message) {
            super(message);
        }
    }

    public static class EmptyCartException
            extends RuntimeException {
        public EmptyCartException(String message) {
            super(message);
        }
    }

    public static class ProductOutOfStockException
            extends RuntimeException {
        public ProductOutOfStockException(String message) {
            super(message);
        }
    }

    public static class InvalidQuantityException
            extends RuntimeException {
        public InvalidQuantityException(String message) {
            super(message);
        }
    }

    public static class ProductNotFoundException
            extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserNotLoggedInException
            extends RuntimeException {
        public UserNotLoggedInException(String message) {
            super(message);
        }
    }

    public static class PaymentFailedException
            extends RuntimeException {
        public PaymentFailedException(String message) {
            super(message);
        }
    }
}