package com.splitwise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OwedAmount {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("amount")
    private double amount;
}
