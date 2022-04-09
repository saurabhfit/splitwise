package com.splitwise.model.expense;

import com.splitwise.model.User;
import com.splitwise.model.split.EqualSplit;
import com.splitwise.model.split.Split;

import java.util.List;

public class EqualExpense extends Expense{

    public EqualExpense(double amount, String expensePaidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(amount, expensePaidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        for(Split split: getSplits()){
            if(!(split instanceof EqualSplit)) return false;
        }
        return true;
    }
}
