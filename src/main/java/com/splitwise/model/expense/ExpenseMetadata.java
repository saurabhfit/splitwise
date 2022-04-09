package com.splitwise.model.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExpenseMetadata {

    @JsonProperty("expenseName")
    private String expenseName;

}
