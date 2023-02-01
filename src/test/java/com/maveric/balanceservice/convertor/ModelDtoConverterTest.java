package com.maveric.balanceservice.convertor;


import com.maveric.balanceservice.constant.Currency;
import com.maveric.balanceservice.converter.BalanceMapper;
import com.maveric.balanceservice.dto.BalanceDto;
import com.maveric.balanceservice.model.Balance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ModelDtoConverterTest {

    @InjectMocks
    private BalanceMapper balanceMapper;

    @Test
    void handleEntityToDto() {
        Balance balance =getSampleBalance();
        BalanceDto balanceDto = balanceMapper.entityToDto(balance);
        assertNotNull(balanceDto.getAmount());
    }

    @Test
    void handleDtoToEntity() {
        BalanceDto balanceDto =getSampleDtoBalance();
        Balance balance = balanceMapper.dtoToEntity(balanceDto);
        assertNotNull(balance.getAmount());
    }

    public Balance getSampleBalance(){

        Balance balance = new Balance();
        balance.setCurrency(Currency.INR);
        balance.setAccountId("4");
        balance.setAmount("200");
        return balance;
    }

    public BalanceDto getSampleDtoBalance(){
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setCurrency(Currency.INR);
        balanceDto.setAccountId("1");
        balanceDto.setAmount("200");
        return balanceDto;
    }
}
