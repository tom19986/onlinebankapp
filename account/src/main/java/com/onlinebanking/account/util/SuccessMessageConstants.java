package com.onlinebanking.account.util;


import com.onlinebanking.account.model.AccountStatus;
import com.onlinebanking.account.model.AccountType;

public class SuccessMessageConstants {

    // Account-related success messages
    public static final String ACCOUNT_CREATED_SUCCESS = "Account created successfully.";
    public static final String ACCOUNT_DELETED_SUCCESS = "Account deleted successfully.";
    public static final String ACCOUNT_UPDATED_SUCCESS = "Account updated successfully.";
    public static final String ACCOUNT_FOUND = "Account found.";
    public static final String ACCOUNT_ACTIVATED = "Account activated successfully.";
    public static final String ACCOUNT_CLOSED = "Account closed successfully.";
    public static final String ACCOUNT_TO_CURRENT = "Converted to current Account successfully.";
    public static final String ACCOUNT_TO_SAVINGS = "Converted to savings Account successfully.";
    public static final String BALANCE_UPDATED = "Balance successfully updated.";

    // Dynamic message for status update
    public static String STATUS_UPDATE_MSG(AccountStatus status) {
        return "Account status successfully updated to " + status + ".";
    }

    // Dynamic message for type update
    public static String TYPE_UPDATE_MSG(AccountType type) {
        return "Account type successfully updated to " + type + ".";
    }

}
