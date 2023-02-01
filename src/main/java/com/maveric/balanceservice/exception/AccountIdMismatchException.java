package com.maveric.balanceservice.exception;

public class AccountIdMismatchException extends RuntimeException {
    public AccountIdMismatchException(String accountId){
        super("User not belongs to account id "+ accountId);
    }

    public AccountIdMismatchException(String accountId,String message){
        super(accountId+" :"+message);
    }
}
