package com.splitwise.service;

import com.splitwise.Repository.BalanceRepository;
import com.splitwise.Repository.ExpenseRepository;
import com.splitwise.Repository.UserRepository;
import com.splitwise.model.DTO.ExpenseDTO;
import com.splitwise.model.OwedAmount;
import com.splitwise.model.User;
import com.splitwise.model.balance.Balance;
import com.splitwise.model.expense.*;
import com.splitwise.model.split.EqualSplit;
import com.splitwise.model.split.ExactSplit;
import com.splitwise.model.split.PercentSplit;
import com.splitwise.model.split.Split;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    BalanceRepository balanceRepository;

    public String processQuery(ExpenseDTO expenseDTO) {
//        EXPENSE u1 1000 4 u1 u2 u3 u4 EQUAL
//        "expenseQuery": "EXPENSE saurabhfit 1000 4 saurabhfit syedfit rishavfit amitfit EQUAL"
//        EXPENSE u1 1250 2 u2 u3 EXACT 370 880
//        "expenseQuery": "EXPENSE saurabhfit 1250 2 syedfit rishavfit EXACT 370 880"
//        EXPENSE u4 1200 4 u1 u2 u3 u4 PERCENT 40 20 20 20
//        "expenseQuery": "EXPENSE amitfit 1200 4 saurabhfit syedfit rishavfit amitfit PERCENT 40 20 20 20"
        try {
            String query = expenseDTO.getExpenseQuery();
            StringTokenizer str = new StringTokenizer(query);
            String EXPENSE = str.nextToken();
            String userWhoPaid = userRepository.findByUserName(str.nextToken()).get(0).getId();
            double amount = Double.parseDouble(str.nextToken());
            int numberOfPersonInvolved = Integer.parseInt(str.nextToken());
            List<String> usersInvolved = new ArrayList<>();
            for (int i = 0; i < numberOfPersonInvolved; i++) {
                usersInvolved.add(userRepository.findByUserName(str.nextToken()).get(0).getId());
            }
            String option = str.nextToken();
            List<Split> splits = new ArrayList<>();
            ExpenseMetadata metadata = new ExpenseMetadata();
            switch (option) {
                case "EQUAL":
                    double amountSharedByEach = amount / numberOfPersonInvolved;

                    for(String userId : usersInvolved){
                        Split split = new EqualSplit(userId);
                        split.setAmount(amountSharedByEach);
                        splits.add(split);
                    }
                    metadata.setExpenseName("Bill with Equal Split");
                    EqualExpense equalExpense = new EqualExpense(amount, userWhoPaid, splits, metadata);
                    if(equalExpense.validate()){
                        expenseRepository.save(equalExpense);
                        updateBalances(equalExpense);
                    }
                    break;

                case "EXACT":
                    for(int i=0; i<numberOfPersonInvolved; i++){
                        double splitAmount = Double.parseDouble(str.nextToken());
                        Split split = new ExactSplit(usersInvolved.get(i));
                        split.setAmount(splitAmount);
                        splits.add(split);
                    }
                    metadata.setExpenseName("Bill with Exact Split");
                    ExactExpense exactExpense = new ExactExpense(amount, userWhoPaid, splits, metadata);
                    if(exactExpense.validate()){
                        expenseRepository.save(exactExpense);
                        updateBalances(exactExpense);
                    }
                    break;

                case "PERCENT":
                    for(int i=0; i<numberOfPersonInvolved; i++){
                        double splitPercent = Double.parseDouble(str.nextToken());
                        double splitAmount = (splitPercent/100)*amount;
                        Split split = new PercentSplit(usersInvolved.get(i), splitPercent);
                        split.setAmount(splitAmount);
                        splits.add(split);
                    }
                    metadata.setExpenseName("Bill with Percent Split");
                    PercentageExpense percentageExpense = new PercentageExpense(amount, userWhoPaid, splits, metadata);
                    if(percentageExpense.validate()){
                        expenseRepository.save(percentageExpense);
                        updateBalances(percentageExpense);
                    }
                    break;

                default:
                    return "Which case you want";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "Failed";
        }
        return "Success";
    }

    private void updateBalances(Expense expense){
        Balance balance = null;
        try{
            balance = balanceRepository.findByUserId(expense.getExpensePaidBy()).get(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(balance==null){
            balance = new Balance();
        }
        balance.setUserId(expense.getExpensePaidBy());
        for(Split split : expense.getSplits()){
            double splitAmount = split.getAmount();
            if(!split.getUserId().equals(expense.getExpensePaidBy())){
                Balance owedBalance = null;
                try{
                    owedBalance = balanceRepository.findByUserId(split.getUserId()).get(0);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(owedBalance!=null && owedBalance.getOwedAmounts().stream().anyMatch(item -> item.getUserId().equals(expense.getExpensePaidBy()))){
                    double prevAmount = owedBalance.getOwedAmounts().stream().filter(e->e.getUserId().equals(expense.getExpensePaidBy())).map(e->e.getAmount()).findFirst().orElse(0.0);
                    if(prevAmount-splitAmount<=0){
                        List<OwedAmount> list = owedBalance.getOwedAmounts();
                        list = list.stream().filter(e-> !e.getUserId().equals(expense.getExpensePaidBy())).collect(Collectors.toList());
                        owedBalance.setOwedAmounts(list);
                        balanceRepository.save(owedBalance);
                        splitAmount -= prevAmount;
                    }else if(prevAmount-splitAmount>0){
                        OwedAmount owedAmount = owedBalance.getOwedAmounts().stream().filter(e->e.getUserId().equals(expense.getExpensePaidBy())).findFirst().get();
                        owedAmount.setAmount(prevAmount - splitAmount);
                        balanceRepository.save(owedBalance);
                        splitAmount = 0;
                    }
                }
                if(splitAmount>0){
                    if(balance.getOwedAmounts().stream().anyMatch(item -> item.getUserId().equals(split.getUserId()))){
                        double prevAmount = balance.getOwedAmounts().stream().filter(e-> e.getUserId().equals(split.getUserId())).map(e -> e.getAmount()).findFirst().orElse(0.0);
                        OwedAmount owedAmount = balance.getOwedAmounts().stream().filter(e->e.getUserId().equals(split.getUserId())).findFirst().get();
                        owedAmount.setAmount( prevAmount + splitAmount );
                    }else{
                        OwedAmount owedAmount = new OwedAmount();
                        owedAmount.setUserId(split.getUserId());
                        owedAmount.setAmount(splitAmount);
                        balance.getOwedAmounts().add(owedAmount);
                    }
                    balanceRepository.save(balance);
                }
            }
        }
    }

    public List<Expense> getAllExpense() {
        return expenseRepository.findAll();
    }
}


