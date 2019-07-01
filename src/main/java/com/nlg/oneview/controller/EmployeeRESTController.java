package com.nlg.oneview.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlg.oneview.exception.RecordNotFoundException;
import com.nlg.oneview.model.Employee;
import com.nlg.oneview.repository.EmployeeRepository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
@RequestMapping(value = "/employee-management", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class EmployeeRESTController {
	@Autowired
	private EmployeeRepository repository;

	public EmployeeRepository getRepository() {
		return repository;
	}

	public void setRepository(EmployeeRepository repository) {
		this.repository = repository;
	}

	@GetMapping(value = "/employees")
	public List<Employee> getAllEmployees() {
		return repository.findAll();
	}

	@PostMapping("/employees")
	Employee createOrSaveEmployee(@RequestBody Employee newEmployee) {
		return repository.save(newEmployee);
	}

	@GetMapping("/employees/{id}")
	Employee getEmployeeById(
			@PathVariable @Min(value = 1, message = "id must be greater than or equal to 1") @Max(value = 1000, message = "id must be lower than or equal to 1000") Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Employee id '" + id + "' does no exist"));
	}

	@PutMapping("/employees/{id}")
	Employee updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

		return repository.findById(id).map(employee -> {
			employee.setFirstName(newEmployee.getFirstName());
			employee.setLastName(newEmployee.getLastName());
			employee.setEmail(newEmployee.getEmail());
			return repository.save(employee);
		}).orElseGet(() -> {
			newEmployee.setId(id);
			return repository.save(newEmployee);
		});
	}

	@DeleteMapping("/employees/{id}")
	void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}

	@PostMapping("/employees/special")
	public void externalCall(@RequestBody Employee employee) throws IOException {

		OkHttpClient client = new OkHttpClient();
		ObjectMapper _payload = new ObjectMapper();
		String payload = _payload.writeValueAsString(employee);
		okhttp3.RequestBody body = okhttp3.RequestBody.create(payload,
				okhttp3.MediaType.parse("application/json; charset=utf-8"));
		System.out.println(body.toString());
		System.out.println(payload.toString());
		Request request = new Request.Builder().url("http://localhost:8090/employee-management/employees").post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			String _response = response.body().toString();
			System.out.println(_response);
//			ObjectMapper _employee = new ObjectMapper();
//			Employee newEmployee = _employee.readValue(_response, Employee.class);
//			return newEmployee;
		}
	}
}