package com.maveric.balanceservice.exception;
public class BalanceNotFoundException extends RuntimeException{
    public BalanceNotFoundException(String id){
        super("Could not find balance id "+ id);
    }
}
