package com.maveric.balanceservice.constant;

public class ErrorSuccessMessageConstants {
    private ErrorSuccessMessageConstants(){

    }
    public static final String MISSING_INPUT = "Input is missing or not valid";
    public static final String CURRENCY_ERROR = "Currency should one of the following value [DOLLAR, INR, EURO]";
    public static final String ACCOUNT_ID_MISMATCH = "account id in url and request body should be same";

    public static final String DELETE_SUCCESS_MESSAGE = "Balance Deleted Successfully";
    public static final String GET_BALANCE_LOG = "getBalanceDetails-> balance fetched for accountId ";
    public static final String GET_ALL_BALANCE_LOG = "getAllBalance-> all balance fetched for accountId ";
    public static final String DELETE_BALANCE_LOG = "deleteBalance-> Balance deleted for accountId ";
    public static final String UPDATE_BALANCE_LOG = "updateBalance-> balance updated for accountID ";
    public static final String UPDATE_BALANCE_ERROR_LOG = "updateBalance-> accountId given in request body and url are not same ";
    public static final String CREATE_BALANCE_LOG = "createBalance-> balance created for accountID ";
    public static final String CREATE_BALANCE_ERROR_LOG = "createBalance-> accountId given in request body and url are not same";
    public static final String CREATEV1_BALANCE_LOG = "createBalanceForAccount-> balance created for accountID ";
    public static final String CREATEV1_BALANCE_ERROR_LOG = "createBalanceForAccount-> accountId given in request body and url are not samey";
    public static final String ACCOUNT_ID_NOT_BLANK = "accountId shouldn't be empty";
    public static final String AMOUNT_NOT_BLANK = "amount shouldn't be empty";
    public static final String AMOUNT_REGEX = "^[+]?(\\d+\\.?\\d*|\\.\\d+)$";
    public static final String AMOUNT_INVALID = "Invalid amount given";

}
