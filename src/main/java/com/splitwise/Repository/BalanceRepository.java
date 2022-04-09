package com.splitwise.Repository;

import com.splitwise.model.User;
import com.splitwise.model.balance.Balance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BalanceRepository extends MongoRepository<Balance, String> {
    List<Balance> findByUserId(String userId);
}
