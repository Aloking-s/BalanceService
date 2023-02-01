package com.maveric.balanceservice.service;

import com.maveric.balanceservice.constant.ErrorSuccessMessageConstants;
import com.maveric.balanceservice.exception.AccountIdMismatchException;
import com.maveric.balanceservice.exception.BalanceAlreadyExistException;
import com.maveric.balanceservice.exception.BalanceNotFoundException;
import com.maveric.balanceservice.model.Account;
import com.maveric.balanceservice.model.Balance;
import com.maveric.balanceservice.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.maveric.balanceservice.converter.BalanceMapper;
import com.maveric.balanceservice.dto.BalanceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BalanceService {
    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    BalanceMapper balanceMapper;

    public AggregationExpression getBalance(String balanceId, String accountId) {

        Balance balance = balanceRepository.findByAccountIdAndBalanceId(balanceId, accountId).orElseThrow(() -> new BalanceNotFoundException(balanceId));
        return balance.getAmount();

    }

    public List<BalanceDto> getAllBalance(String accountId, int page, int pageSize){

        Page<Balance> balances = balanceRepository.findAllByAccountId(PageRequest.of(page, pageSize),accountId);
        List<Balance> listBalance = balances.getContent();
        return balanceMapper.entityToDtoList(listBalance);
    }


    public AggregationExpression deleteBalance(String balanceId, String accountId){
        balanceRepository.findByAccountIdAndBalanceIdWithDelete(balanceId,accountId).orElseThrow(() -> new BalanceNotFoundException(balanceId));
        return ErrorSuccessMessageConstants.DELETE_SUCCESS_MESSAGE;
    }

    public BalanceDto updateBalance(Balance balance, String balanceId){
        Optional<Balance> balanceFromDb = balanceRepository.findById(balanceId);
            if(balanceFromDb.isPresent()) {
            Balance newBal = balanceFromDb.get();
            newBal.setAccountId(balance.getAccountId());
            newBal.setCurrency(balance.getCurrency());
            newBal.setAmount(balance.getAmount());


            return balanceMapper.entityToDto(balanceRepository.save(newBal));
        }else{
                throw  new BalanceNotFoundException(balanceId);
            }
    }

    public BalanceDto createBalance(Balance balance){

       return balanceMapper.entityToDto(balanceRepository.save(balance));
    }

    public void findAccountIdBelongsToCurrentUser(List<Account> account,String account_id){
        AtomicInteger count = new AtomicInteger(0);
        account.forEach(singleAccount -> {
            if (singleAccount.get_id().equals(account_id)){
                count.getAndIncrement();
            }
        });
        if(count.get() == 0){
            throw new AccountIdMismatchException(account_id);
        }
    }
    public BalanceDto getBalanceForParticularAccount(String accountId){
        Balance balances = balanceRepository.findByAccountId(accountId);
        return balanceMapper.entityToDto(balances);

    }

    public BalanceDto createBalanceForAccount(Balance balance){
        Balance balance1=balanceRepository.findByAccountId(balance.getAccountId());
        if(balance1 == null)
            return balanceMapper.entityToDto(balanceRepository.save(balance));

        throw new BalanceAlreadyExistException(balance.getAccountId());
    }
}
