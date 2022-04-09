package com.splitwise.model.balance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.splitwise.model.OwedAmount;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Document(collection = "Balance")
public class Balance {
    @JsonProperty("id")
    @Id
    private String id;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("owedAmounts")
    private List<OwedAmount> owedAmounts = new ArrayList<>();

}
