package com.splitwise.model.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ResponsePayload {
    public enum RESPONSE_STATUS { SUCCESS, FAILURE };
    private RESPONSE_STATUS responseStatus;
    private String responseMessage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Object> responseDetails = new ArrayList<>();

    public ResponsePayload(RESPONSE_STATUS response_status, String responseMessage){
        this.responseStatus = response_status;
        this.responseMessage = responseMessage;
    }

    public void addResponseDetails(Object responseDetail){
        if(responseDetails!=null){
            responseDetails.add(responseDetail);
        }
    }
}
