package com.maveric.balanceservice.Feignclient;

import com.maveric.balanceservice.model.Account;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
    @FeignClient(value = "feignAccount",url = "http://localhost:3010/api/v1/")
    public interface AccountFeignService {
        @GetMapping("/customers/{customerId}/customerAccounts")
        ResponseEntity<List<Account>> getAccountsbyId(@PathVariable String customerId);
}
