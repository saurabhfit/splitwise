package com.splitwise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
        "_id",
        "userName",
        "email",
        "mobileNumber"
})
@Data
@Document(collection = "User")
public class User {

    @JsonProperty("_id")
    @Id
    private String id;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    public User(String userName, String email, String mobileNumber) {
        this.userName = userName;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }
}
