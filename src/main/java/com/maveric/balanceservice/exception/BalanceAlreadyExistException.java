package com.maveric.balanceservice.exception;

public class BalanceAlreadyExistException extends RuntimeException{
    public BalanceAlreadyExistException(String id){
        super("account already exist with this id "+ id);
    }
}
