package com.mentortheyoung.moneyflow.repositories;

import com.mentortheyoung.moneyflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
