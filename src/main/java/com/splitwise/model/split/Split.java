package com.splitwise.model.split;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.splitwise.model.User;
import lombok.Data;

@Data
public abstract class Split {
    private String userId;
    private double amount;
    Split(String userTheAmountWasSentTo){
        userId = userTheAmountWasSentTo;
    }
}
