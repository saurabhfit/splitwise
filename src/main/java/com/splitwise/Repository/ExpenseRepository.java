package com.splitwise.Repository;

import com.splitwise.model.expense.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ExpenseRepository extends MongoRepository<Expense, String> {

}
