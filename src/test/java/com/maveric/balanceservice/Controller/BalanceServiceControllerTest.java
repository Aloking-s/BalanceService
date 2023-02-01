package com.maveric.balanceservice.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.balanceservice.Feignclient.AccountFeignService;
import com.maveric.balanceservice.constant.AccountType;
import com.maveric.balanceservice.constant.Currency;
import com.maveric.balanceservice.controller.BalanceServiceController;
import com.maveric.balanceservice.exception.BalanceNotFoundException;
import com.maveric.balanceservice.model.Account;
import com.maveric.balanceservice.model.Balance;
import com.maveric.balanceservice.repository.BalanceRepository;
import com.maveric.balanceservice.service.BalanceService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Switch.CaseOperator.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BalanceServiceController.class)
@Tag("Integration tests")
public class BalanceServiceControllerTest {

    private static final String API_V1_BALANCE = "/api/v1/accounts/4/balances";

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Mock
    private List<Account> account;

    @MockBean
    private BalanceService balanceService;

    @MockBean
    private BalanceRepository balanceRepository;

    @MockBean
    private AccountFeignService accountFeignService;

    @Test
    void shouldGetBalanceWhenRequestMadeToGetBalance() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        mvc.perform(get(API_V1_BALANCE+"/631061c4c45f78545a1ed042").header("userId",1))
                .andExpect(status().isOk())
                .andDo(print());

    }
    @Test
    void shouldGetBalancesWhenRequestMadeToGetBalances() throws Exception {
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        mvc.perform(get(API_V1_BALANCE + "?page=0&pageSize=10").header("userId",1))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnInternalServerWhenDbReturnsErrorForGetBalances() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        when((AggregationExpression) balanceService.getAllBalance("1",0,10)).then(new IllegalArgumentException());
        mvc.perform(get(API_V1_BALANCE+"?page=-1").header("userId",1))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }
    @Test
    void shouldDeleteBalanceWhenRequestMadeToDeleteBalance() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        mvc.perform(delete(API_V1_BALANCE+"/631061c4c45f78545a1ed042").header("userId",1))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void shouldReturnInternalServerWhenDbReturnsErrorForBalance() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        when(balanceService.getBalance(any(),any())).then(new BalanceNotFoundException("631061c4c45f78545a1ed042"));
        mvc.perform(get(API_V1_BALANCE+"/631061c4c45f78545a1ed04","1").header("userId",1))
                .andExpect(status().isNotFound())
                .andDo(print());

    }
    @Test
    void shouldReturnInternalServerWhenDbReturnsErrorForDelete() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        when(balanceService.deleteBalance(any(),any())).then(new BalanceNotFoundException("631061c4c45f78545a1ed04"));
        mvc.perform(delete(API_V1_BALANCE+"/631061c4c45f78545a1ed04").header("userId",1))
                .andExpect(status().isNotFound())
                .andDo(print());

    }
    @Test
    void shouldUpdateBalanceWhenRequestMadeToUpdateBalance() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        mvc.perform(put(API_V1_BALANCE+"/631061c4c45f78545a1ed042").header("userId",1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(getSampleBalance())))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void shouldCreateBalanceWhenRequestMadeToCreateBalance() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        mvc.perform(post(API_V1_BALANCE).contentType(MediaType.APPLICATION_JSON).header("userId",1).content(mapper.writeValueAsString(getSampleBalance())))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldThrowBadRequestWhenBalanceDetailsAreWrongForUpdate() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        Balance balance = new Balance();
        balance.setCurrency(Currency.INR);
        balance.setAccountId(null);
        balance.setAmount("200");
        mvc.perform(put(API_V1_BALANCE+"/631061c4c45f78545a1ed042").header("userId",1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(balance)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void shouldThrowBadRequestWhenBalanceDetailsAreWrongForCreate() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        Balance balance = new Balance();
        balance.setCurrency(Currency.INR);
        balance.setAccountId(null);
        balance.setAmount("200");
        mvc.perform(post(API_V1_BALANCE).header("userId",1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(balance)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void shouldReturnInternalServerWhenDbReturnsErrorForUpdate() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        when((AggregationExpression) balanceService.updateBalance(any(Balance.class),eq("631061c4c45f78545a1ed042"))).then(new IllegalArgumentException());
        mvc.perform(post(API_V1_BALANCE+"/631061c4c45f78545a1ed042").header("userId",1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(getSampleBalance())))
                .andExpect(status().isInternalServerError())
                .andDo(print());

    }
    @Test
    void shouldReturnInternalServerWhenDbReturnsErrorForCreate() throws Exception{
        when((AggregationExpression) accountFeignService.getAccountsbyId("1")).then(getSampleAccount());
        when((AggregationExpression) balanceService.createBalanceForAccount(any(Balance.class))).then(new IllegalArgumentException());
        mvc.perform(post(API_V1_BALANCE).header("userId",1).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(getSampleBalance())))
                .andExpect(status().isInternalServerError())
                .andDo(print());

    }


    public Object getSampleBalance(){

        Balance balance = new Balance();
        balance.setCurrency(Currency.INR);
        balance.setAccountId("4");
        balance.setAmount("200");
        return balance;
    }

    public Object getSampleAccount(){

        List<Account> accountList = new ArrayList<>();
        Account account = new Account();
        account.setCustomerId("1");
        account.setAccountType(AccountType.CURRENT);
        Account account1 = new Account();
        account1.setCustomerId("2");
        account1.setAccountType(AccountType.CURRENT);

        accountList.add(account1);
        accountList.add(account);
        return ResponseEntity.status(HttpStatus.OK).body(accountList);
    }
}
