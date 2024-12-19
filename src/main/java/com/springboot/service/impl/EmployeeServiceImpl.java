package com.springboot.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.springboot.dto.EmployeeDto;
import com.springboot.entity.Employee;
import com.springboot.repository.EmployeeRepository;
import com.springboot.service.EmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;
	private ModelMapper modelMapper;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
		super();
		this.employeeRepository = employeeRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
		Employee employee = modelMapper.map(employeeDto, Employee.class);
		Mono<Employee> savedEmployee = employeeRepository.save(employee);
		return savedEmployee.map((entity) -> modelMapper.map(entity, EmployeeDto.class));
	}

	@Override
	public Mono<EmployeeDto> getEmployee(String id) {
		Mono<Employee> byId = employeeRepository.findById(id);
		return byId.map((employee) -> modelMapper.map(employee, EmployeeDto.class));
	}

	@Override
	public Flux<EmployeeDto> getAllEmployee() {
		Flux<Employee> all = employeeRepository.findAll();
		return all.map((employee) -> modelMapper.map(employee, EmployeeDto.class)).switchIfEmpty(Flux.empty());

	}

	@Override
	public Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String id) {
		Mono<Employee> employee = employeeRepository.findById(id);

		Mono<Employee> updatedEmployee = employee.flatMap((emp) -> {
			emp.setFirstName(employeeDto.getFirstName());
			emp.setLastName(employeeDto.getLastName());
			emp.setEmail(employeeDto.getEmail());

			return employeeRepository.save(emp);
		});

		return updatedEmployee.map((emp) -> modelMapper.map(emp, EmployeeDto.class));
	}

	@Override
	public Mono<Void> deleteEmployee(String id) {
		return employeeRepository.deleteById(id);
	}

}
