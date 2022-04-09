package com.splitwise.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.splitwise.model.User;
import com.splitwise.model.expense.ExpenseMetadata;
import com.splitwise.model.split.Split;
import lombok.Data;

import java.util.List;

@Data
public class ExpenseDTO {

    @JsonProperty("expenseQuery")
    private String expenseQuery;

}
