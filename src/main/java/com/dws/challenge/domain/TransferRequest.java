package com.dws.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull(message = "Account Id Should Not Null or Empty")
    @NotEmpty(message = "Account Id Should Not Null or Empty")
    private final String fromAccountId;

    @NotNull(message = "Account Id Should Not Null or Empty")
    @NotEmpty(message = "Account Id Should Not Null or Empty")
    private final String toAccountId;

    @NotNull
    @Min(value = 1, message = "Minimum Amount Should be grater than 1.")
    private BigDecimal transferAmount;


    @JsonCreator
    public TransferRequest(@JsonProperty("fromAccountId") String fromAccountId, @JsonProperty("toAccountId") String toAccountId, @JsonProperty("transferAmount") BigDecimal transferAmount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transferAmount = transferAmount;
    }
}
