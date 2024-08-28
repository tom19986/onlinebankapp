package com.onlinebanking.card.util;

public final class ErrorMessageUtil {

    private ErrorMessageUtil() {
        // Private constructor to prevent instantiation
    }

    public static final String CARD_NOT_FOUND = "Card not found.";
    public static final String INVALID_CARD_OPERATION = "Invalid operation on the card.";
    public static final String AMOUNT_MUST_BE_GREATER_THAN_ZERO = "Amount must be greater than zero.";
    public static final String CARD_ALREADY_DEACTIVATED = "Card is already deactivated.";



    public static final String AMOUNT_MUST_BE_GREATER_THAN_OR_EQUAL_TO_ZERO = "Amount must be greater than or equal to zero.";
    // Add more error messages as needed
}
