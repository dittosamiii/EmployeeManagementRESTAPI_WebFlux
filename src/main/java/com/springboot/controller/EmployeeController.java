package com.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.dto.EmployeeDto;
import com.springboot.service.EmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

	private EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Mono<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) {
		return employeeService.saveEmployee(employeeDto);
	}

	@GetMapping("{id}")
	public Mono<EmployeeDto> getEmployee(@PathVariable String id) {
		return employeeService.getEmployee(id);
	}

	@GetMapping
	public Flux<EmployeeDto> getAllEmployees() {
		return employeeService.getAllEmployee();
	}

	@PutMapping("{id}")
	public Mono<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable String id) {
		return employeeService.updateEmployee(employeeDto, id);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public Mono<Void> deleteEmployee(@PathVariable String id) {
		return employeeService.deleteEmployee(id);
	}

}
