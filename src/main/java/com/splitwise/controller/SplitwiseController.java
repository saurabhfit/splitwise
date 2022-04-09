package com.splitwise.controller;

import com.splitwise.model.DTO.ExpenseDTO;
import com.splitwise.model.expense.Expense;
import com.splitwise.model.payload.ResponsePayload;
import com.splitwise.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/1.0/splitwise")
public class SplitwiseController {

    @Autowired
    ExpenseService expenseService;

    @RequestMapping(value = "/addExpense", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePayload> addExpense(@RequestBody ExpenseDTO expenseDTO){
        ResponsePayload payload = new ResponsePayload();
        if(expenseDTO.getExpenseQuery()!=null){
            String result = expenseService.processQuery(expenseDTO);
            payload.setResponseMessage("Query "+result);
            payload.setResponseStatus(ResponsePayload.RESPONSE_STATUS.SUCCESS);
            return ResponseEntity.ok().body(payload);
        }else{
            payload.setResponseMessage("expense query cannot be blank");
            payload.setResponseStatus(ResponsePayload.RESPONSE_STATUS.FAILURE);
            return ResponseEntity.badRequest().body(payload);
        }
    }

    @RequestMapping(value = "/showAllExpense", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePayload> showAllExpense(){
        ResponsePayload payload = new ResponsePayload();
        List<Expense> expenses = expenseService.getAllExpense();
        if(expenses==null){
            payload.setResponseMessage("Failed to fetch expenses");
            payload.setResponseStatus(ResponsePayload.RESPONSE_STATUS.FAILURE);
            return ResponseEntity.internalServerError().body(payload);
        }else if(expenses.size()==0){
            payload.setResponseMessage("No expenses yet");
            payload.setResponseStatus(ResponsePayload.RESPONSE_STATUS.SUCCESS);
            return ResponseEntity.ok().body(payload);
        }else{
            payload.setResponseMessage("Showing all expenses");
            payload.setResponseStatus(ResponsePayload.RESPONSE_STATUS.SUCCESS);
            payload.addResponseDetails(expenses);
            return ResponseEntity.ok().body(payload);
        }
    }

}
