package com.splitwise.model.expense;

import com.splitwise.model.User;
import com.splitwise.model.split.ExactSplit;
import com.splitwise.model.split.Split;

import java.util.List;

public class ExactExpense extends Expense{

    public ExactExpense(double amount, String expensePaidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(amount, expensePaidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        double totalAmount = getAmount();
        double totalSplitAmount = 0;
        for(Split split: getSplits()){
            if(!(split instanceof ExactSplit))  return false;
            totalSplitAmount += split.getAmount();
        }
        return totalAmount == totalSplitAmount;
    }
}
