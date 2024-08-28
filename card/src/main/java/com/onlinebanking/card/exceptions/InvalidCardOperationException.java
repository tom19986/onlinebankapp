package com.onlinebanking.card.exceptions;

public class InvalidCardOperationException extends RuntimeException {
    public InvalidCardOperationException(String message) {
        super(message);
    }
}
