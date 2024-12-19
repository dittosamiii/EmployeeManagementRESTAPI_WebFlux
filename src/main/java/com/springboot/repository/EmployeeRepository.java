package com.springboot.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.springboot.entity.Employee;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {

}
