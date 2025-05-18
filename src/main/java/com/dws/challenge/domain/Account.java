package com.dws.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Data;
import lombok.Locked;

@Data
public class Account {

    @NotNull(message = "Account Id Should Not Null or Empty")
    @NotEmpty(message = "Account Id Should Not Null or Empty")
    private final String accountId;

    @NotNull(message = "Account Id Should Not Null or Empty")
    @Min(value = 0, message = "Initial balance must be positive.")
    private BigDecimal balance;

    @JsonIgnore
    private final ReentrantLock lock = new ReentrantLock();


    public Account(String accountId) {
        this.accountId = accountId;
        this.balance = BigDecimal.ZERO;
    }

    @JsonCreator
    public Account(@JsonProperty("accountId") String accountId, @JsonProperty("balance") BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }
}
