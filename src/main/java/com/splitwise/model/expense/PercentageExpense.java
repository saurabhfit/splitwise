package com.splitwise.model.expense;

import com.splitwise.model.User;
import com.splitwise.model.split.PercentSplit;
import com.splitwise.model.split.Split;

import java.util.List;

public class PercentageExpense extends Expense{

    public PercentageExpense(double amount, String expensePaidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(amount, expensePaidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        double totalSplitPercent = 0;
        for(Split split : getSplits()){
            if(!(split instanceof PercentSplit))    return false;
            totalSplitPercent += ((PercentSplit) split).getPercent();
        }
        return 100.0 == totalSplitPercent;
    }
}
