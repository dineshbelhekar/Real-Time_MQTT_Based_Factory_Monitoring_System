package com.example.IM.PT.repository;

import com.example.IM.PT.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

     User findByUsername(String Username);

     void deleteByemployeeId(String employeeId);

     Optional<User> findByEmployeeId(String employeeId);

     List<User> findByDepartment(String department);
}
