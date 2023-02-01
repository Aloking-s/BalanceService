package com.maveric.balanceservice.dto;

import com.maveric.balanceservice.constant.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class BalanceDto {
    @Id
    private String id;

    private String accountId;

    private String amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Date createdAt;

    private Date updatedAt;
}
