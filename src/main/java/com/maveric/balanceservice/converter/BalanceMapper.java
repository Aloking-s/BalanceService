package com.maveric.balanceservice.converter;

import com.maveric.balanceservice.dto.BalanceDto;
import com.maveric.balanceservice.model.Balance;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BalanceMapper {
    public List<BalanceDto> entityToDtoList(List<Balance> balances) {
        List<BalanceDto> balanceDto = new ArrayList<>();
        balances.stream().forEach(balance -> {
            BalanceDto singleBalance = new BalanceDto();
            singleBalance.setCreatedAt(balance.getCreatedAt());
            singleBalance.setId(balance.getId());
            singleBalance.setUpdatedAt(balance.getUpdatedAt());
            singleBalance.setAmount(balance.getAmount());
            singleBalance.setCurrency(balance.getCurrency());
            singleBalance.setAccountId(balance.getAccountId());

            balanceDto.add(singleBalance);
        });
        return balanceDto;
    }

    public BalanceDto entityToDto(Balance balance){
        BalanceDto balanceDto= new BalanceDto();

        balanceDto.setAccountId(balance.getAccountId());
        balanceDto.setCurrency(balance.getCurrency());
        balanceDto.setAmount(balance.getAmount());
        balanceDto.setId(balance.getId());
        balanceDto.setUpdatedAt(balance.getUpdatedAt());
        balanceDto.setCreatedAt(balance.getCreatedAt());

        return balanceDto;

    }

    public Balance dtoToEntity(BalanceDto balanceDto){
        Balance balance = new Balance();

        balance.setAccountId(balanceDto.getAccountId());
        balance.setAmount(balanceDto.getAmount());
        balance.setCurrency(balanceDto.getCurrency());

        return balance;
    }
}
