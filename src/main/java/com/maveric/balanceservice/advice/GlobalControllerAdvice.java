package com.maveric.balanceservice.advice;

import com.maveric.balanceservice.constant.ErrorSuccessMessageConstants;
import com.maveric.balanceservice.dto.Error;
import com.maveric.balanceservice.exception.AccountIdMismatchException;
import com.maveric.balanceservice.exception.BalanceAlreadyExistException;
import com.maveric.balanceservice.exception.BalanceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> internalServerError(Exception exception){
        Error error = getError(String.valueOf(exception.getMessage()),String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BalanceNotFoundException.class)
    public ResponseEntity<Error>  balanceNotFound(BalanceNotFoundException balanceNotFoundException){
        Error error = getError(balanceNotFoundException.getMessage(),String.valueOf(HttpStatus.NOT_FOUND));
        logger.error(balanceNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccountIdMismatchException.class)
    public ResponseEntity<Error>  accountUserMismatch(AccountIdMismatchException accountIdMismatchException){
        Error error = getError(accountIdMismatchException.getMessage(),String.valueOf(HttpStatus.NOT_FOUND));
        logger.error(accountIdMismatchException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BalanceAlreadyExistException.class)
    public ResponseEntity<Error>  handleDuplicateInput(BalanceAlreadyExistException balanceAlreadyExistException){
        Error error = getError(balanceAlreadyExistException.getMessage(),String.valueOf(HttpStatus.BAD_REQUEST));
        logger.error(balanceAlreadyExistException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error>  handleNullInput(MethodArgumentNotValidException methodArgumentNotValidException){
        Error error = getError(methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage(),String.valueOf(HttpStatus.BAD_REQUEST));
        logger.error(methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpServerErrorException.ServiceUnavailable.class)
    public ResponseEntity<Error>  serviceUnavailable(HttpServerErrorException.ServiceUnavailable serviceUnavailable){
        Error error = getError(String.valueOf(serviceUnavailable.getMessage()),String.valueOf(HttpStatus.SERVICE_UNAVAILABLE));
        logger.error(serviceUnavailable.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error>  handleEnumException(HttpMessageNotReadableException httpMessageNotReadableException){
        Error error = getError(ErrorSuccessMessageConstants.CURRENCY_ERROR,String.valueOf(HttpStatus.BAD_REQUEST));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }



    private Error getError(String message , String code){
        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        return error;

    }
}
