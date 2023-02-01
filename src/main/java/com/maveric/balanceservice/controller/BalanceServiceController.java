package com.maveric.balanceservice.controller;

import com.maveric.balanceservice.Feignclient.AccountFeignService;
import com.maveric.balanceservice.constant.ErrorSuccessMessageConstants;
import com.maveric.balanceservice.dto.BalanceDto;
import com.maveric.balanceservice.exception.AccountIdMismatchException;
import com.maveric.balanceservice.model.Account;
import com.maveric.balanceservice.model.Balance;
import com.maveric.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class BalanceServiceController {

    @Autowired
    BalanceService balanceService;

    @Autowired
    AccountFeignService accountFeignService;

    private static final Logger logger = LoggerFactory.getLogger(BalanceServiceController.class);

    @GetMapping("/accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<Object> getBalanceDetails(@PathVariable String accountId , @PathVariable String balanceId,@RequestHeader(value = "userId") String userId) {
        ResponseEntity<List<Account>> accountList = accountFeignService.getAccountsbyId(userId);
        balanceService.findAccountIdBelongsToCurrentUser(accountList.getBody(),accountId);
        String balance = balanceService.getBalance(balanceId,accountId);
        logger.info(ErrorSuccessMessageConstants.GET_BALANCE_LOG+accountId);
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }
    @GetMapping("/accounts/{accountId}/balances")
    public ResponseEntity<List<BalanceDto>> getAllBalance(@PathVariable String accountId, @RequestParam int page , @RequestParam int pageSize,@RequestHeader(value = "userId") String userId){
        ResponseEntity<List<Account>> accountList = accountFeignService.getAccountsbyId(userId);
        balanceService.findAccountIdBelongsToCurrentUser(accountList.getBody(),accountId);
        List<BalanceDto> balance = balanceService.getAllBalance(accountId,page,pageSize);
        logger.info(ErrorSuccessMessageConstants.GET_ALL_BALANCE_LOG+accountId);
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    @DeleteMapping("/accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<Object> deleteBalance(@PathVariable String balanceId ,@PathVariable String accountId,@RequestHeader(value = "userId") String userId){
        ResponseEntity<List<Account>> accountList = accountFeignService.getAccountsbyId(userId);
        balanceService.findAccountIdBelongsToCurrentUser(accountList.getBody(),accountId);
        String desc = balanceService.deleteBalance(balanceId,accountId);
        logger.info(ErrorSuccessMessageConstants.DELETE_BALANCE_LOG+accountId);
        return ResponseEntity.status(HttpStatus.OK).body(desc);
    }

    @PutMapping("/accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity<BalanceDto> updateBalance(@Valid @RequestBody Balance balance, @PathVariable String accountId, @PathVariable String balanceId,@RequestHeader(value = "userId") String userId) {
        if(balance.getAccountId().equals(accountId)) {
            ResponseEntity<List<Account>> accountList = accountFeignService.getAccountsbyId(userId);
            balanceService.findAccountIdBelongsToCurrentUser(accountList.getBody(), accountId);
            balance.setAccountId(accountId);
            BalanceDto balanceDetails = balanceService.updateBalance(balance, balanceId);
            logger.info(ErrorSuccessMessageConstants.UPDATE_BALANCE_LOG+accountId);
            return ResponseEntity.status(HttpStatus.OK).body(balanceDetails);
        }else{
            logger.error(ErrorSuccessMessageConstants.UPDATE_BALANCE_ERROR_LOG);
            throw new AccountIdMismatchException(accountId, ErrorSuccessMessageConstants.ACCOUNT_ID_MISMATCH);
        }
    }
    @PostMapping("/accounts/{accountId}/balances/balancesAccount")
    public ResponseEntity<BalanceDto> createBalance(@Valid @RequestBody Balance balance, @PathVariable String accountId,@RequestHeader(value = "userId") String userId) {
        if(balance.getAccountId().equals(accountId)) {
            ResponseEntity<List<Account>> accountList = accountFeignService.getAccountsbyId(userId);
            balanceService.findAccountIdBelongsToCurrentUser(accountList.getBody(), accountId);
            BalanceDto balanceDetails = balanceService.createBalance(balance);
            logger.info(ErrorSuccessMessageConstants.CREATE_BALANCE_LOG+accountId);
            return ResponseEntity.status(HttpStatus.CREATED).body(balanceDetails);
        }
        else{
            logger.error(ErrorSuccessMessageConstants.CREATE_BALANCE_ERROR_LOG);
            throw new AccountIdMismatchException(accountId, ErrorSuccessMessageConstants.ACCOUNT_ID_MISMATCH);
        }
    }

    @PostMapping("/accounts/{accountId}/balances")
    public ResponseEntity<BalanceDto> createBalanceForAccount(@Valid @RequestBody Balance balance, @PathVariable String accountId,@RequestHeader(value = "userId") String userId) {
        if(balance.getAccountId().equals(accountId)) {
            ResponseEntity<List<Account>> accountList = accountFeignService.getAccountsbyId(userId);
            balanceService.findAccountIdBelongsToCurrentUser(accountList.getBody(), accountId);
            BalanceDto balanceDetails = balanceService.createBalanceForAccount(balance);
            logger.info(ErrorSuccessMessageConstants.CREATEV1_BALANCE_LOG+accountId);
            return ResponseEntity.status(HttpStatus.CREATED).body(balanceDetails);
        }else{
            logger.error(ErrorSuccessMessageConstants.CREATEV1_BALANCE_ERROR_LOG);
            throw new AccountIdMismatchException(accountId, ErrorSuccessMessageConstants.ACCOUNT_ID_MISMATCH);
        }
    }

    @GetMapping("accounts/{accountId}/balances/accountBalance")
    public ResponseEntity<BalanceDto> getBalanceAccountDetails(@PathVariable String accountId,@RequestHeader(value = "userId") String userId) {
        ResponseEntity<List<Account>> accountList = accountFeignService.getAccountsbyId(userId);
        balanceService.findAccountIdBelongsToCurrentUser(accountList.getBody(),accountId);
        BalanceDto balanceDto = balanceService.getBalanceForParticularAccount(accountId);
        logger.info("getBalanceAccountDetails-> balance detail for accountID {}",accountId);
        return ResponseEntity.status(HttpStatus.OK).body(balanceDto);
    }
}
