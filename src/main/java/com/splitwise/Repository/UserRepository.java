package com.splitwise.Repository;

import com.splitwise.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUserName(String userName);
    List<User> findByEmail(String email);
    List<User> findByMobileNumber(String mobileNumber);
}
