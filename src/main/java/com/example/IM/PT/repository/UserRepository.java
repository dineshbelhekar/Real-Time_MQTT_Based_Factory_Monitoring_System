package com.example.IM.PT.repository;

import com.example.IM.PT.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

     User findByUsername(String Username);


}
