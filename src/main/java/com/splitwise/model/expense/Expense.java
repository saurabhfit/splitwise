package com.splitwise.model.expense;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.splitwise.model.User;
import com.splitwise.model.split.Split;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
        "_id",
        "expenseMetadata",
        "expensePaidBy",
        "splits",
        "amount"
})
@Data
@Document(collection = "Expense")
public abstract class Expense {
    @JsonProperty("_id")
    @Id
    private String id;

    @JsonProperty("expenseMetadata")
    private ExpenseMetadata expenseMetadata;

    @JsonProperty("expensePaidBy")
    private String expensePaidBy;

    @JsonProperty("splits")
    private List<Split> splits;

    @JsonProperty("amount")
    private double amount;

    public Expense(double amount, String expensePaidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        this.expenseMetadata = expenseMetadata;
        this.expensePaidBy = expensePaidBy;
        this.splits = splits;
        this.amount = amount;
    }

    public abstract boolean validate();
}
