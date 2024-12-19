package com.springboot;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.dto.EmployeeDto;
import com.springboot.repository.EmployeeRepository;
import com.springboot.service.EmployeeService;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTests {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private EmployeeRepository employeeRepository;

	@BeforeEach
	public void before() {
		System.out.println("Clearing Database");
		employeeRepository.deleteAll().subscribe();
	}

	@Test
	public void testSaveEmployee() {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setFirstName("John");
		employeeDto.setLastName("Cena");
		employeeDto.setEmail("john@gmail.com");

		webTestClient.post().uri("api/employees").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(Mono.just(employeeDto), EmployeeDto.class).exchange()
				.expectStatus().isCreated().expectBody().consumeWith(System.out::println).jsonPath("$.firstName")
				.isEqualTo(employeeDto.getFirstName()).jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
				.jsonPath("$.email").isEqualTo(employeeDto.getEmail());
	}

	@Test
	public void testGetEmployee() {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setFirstName("Mr");
		employeeDto.setLastName("Bean");
		employeeDto.setEmail("bean@gmail.com");

		EmployeeDto employee = employeeService.saveEmployee(employeeDto).block();

		webTestClient.get().uri("api/employees/{id}", Collections.singletonMap("id", employee.getId())).exchange()
				.expectStatus().isOk().expectBody().consumeWith(System.out::println).jsonPath("$.id")
				.isEqualTo(employee.getId()).jsonPath("$.firstName").isEqualTo(employee.getFirstName())
				.jsonPath("$.lastName").isEqualTo(employee.getLastName()).jsonPath("$.email")
				.isEqualTo(employee.getEmail());
	}

	@Test
	public void testGetAllEmployees() {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setFirstName("Mr");
		employeeDto.setLastName("Bean");
		employeeDto.setEmail("bean@gmail.com");

		employeeService.saveEmployee(employeeDto).block();

		EmployeeDto employeeDto2 = new EmployeeDto();
		employeeDto2.setFirstName("John");
		employeeDto2.setLastName("Cena");
		employeeDto2.setEmail("john@gmail.com");

		employeeService.saveEmployee(employeeDto2).block();

		webTestClient.get().uri("api/employees").exchange().expectStatus().isOk().expectBodyList(EmployeeDto.class)
				.consumeWith(System.out::println);
	}

	@Test
	public void testUpdateEmployee() {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setFirstName("Mr");
		employeeDto.setLastName("Bean");
		employeeDto.setEmail("bean@gmail.com");

		EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

		EmployeeDto employeeDto2 = new EmployeeDto();
		employeeDto2.setFirstName("John");
		employeeDto2.setLastName("Cena");
		employeeDto2.setEmail("john@gmail.com");

		webTestClient.put().uri("api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(employeeDto2), EmployeeDto.class).exchange().expectStatus().isOk().expectBody()
				.consumeWith(System.out::println).jsonPath("$.firstName").isEqualTo(employeeDto2.getFirstName())
				.jsonPath("$.lastName").isEqualTo(employeeDto2.getLastName()).jsonPath("$.email")
				.isEqualTo(employeeDto2.getEmail());
	}

	@Test
	public void testDeleteEmployee() {
		
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setFirstName("Mr");
		employeeDto.setLastName("Bean");
		employeeDto.setEmail("bean@gmail.com");

		EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

		webTestClient.delete().uri("api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
				.exchange().expectStatus().isNoContent().expectBody().consumeWith(System.out::println);

	}

}
