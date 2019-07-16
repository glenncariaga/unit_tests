package com.nlg.oneview;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlg.oneview.controller.EmployeeRESTController;
import com.nlg.oneview.model.Employee;
import com.nlg.oneview.repository.EmployeeRepository;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneViewDataApiApplicationUnitTests {

	private MockWebServer externalAPI;
	private String baseUrl;

	@InjectMocks
	private EmployeeRESTController employeeRESTController;

	@Mock
	EmployeeRepository repository;

	@Before
	public void init() throws IOException {
		MockitoAnnotations.initMocks(this);

		externalAPI = new MockWebServer();
		externalAPI.start();
		HttpUrl _baseUrl = externalAPI.url("/");
		baseUrl = _baseUrl.toString();

		employeeRESTController = new EmployeeRESTController();
		employeeRESTController.setRepository(repository);

		// TODO create data
	}

	@After
	public void shutdown() throws IOException {
//		externalAPI.shutdown();
	}

	@Test
	public void doesExternalCallUseTheHttpConnector() throws Exception {
		// mock data
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");

		// POJO to JSON string parser
		ObjectMapper _payload = new ObjectMapper();
		String payload = _payload.writeValueAsString(emp);

//		 set the mock server for a response
		externalAPI.enqueue(new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json; charset=utf-8").setBody(payload));
		;

		// run the method under test
		Employee employee = employeeRESTController.externalCall(emp, baseUrl);

		// test cases
		Assert.assertEquals(emp.toString(), employee.toString());
	}

	@Test
	public void testGetEmployeeEndpoint() {

		List<Employee> employees = employeeRESTController.getAllEmployees();

		Assert.assertEquals(0, employees.size());
		verify(repository).findAll();
	}

	@Test
	public void testGetEmployeeById() {
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");

		when(repository.findById(1L)).thenReturn(Optional.of(emp));

		Employee employee = employeeRESTController.getEmployeeById(1L);

		Assert.assertEquals(emp.toString(), employee.toString());
		verify(repository).findById(1L);
	}

	@Test
	public void testCreateOrSaveEmployee() {
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");

		when(repository.save(emp)).thenReturn(emp);

		Employee employee = employeeRESTController.createOrSaveEmployee(emp);

		Assert.assertEquals(emp.toString(), employee.toString());
		verify(repository).save(emp);
	}

	@Test
	public void testUpdateEmployeeFoundEmployee() {
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");
		Employee updatedEmp = new Employee(1L, "newFirst", "newLast", "newEmail.com");

		when(repository.findById(1L)).thenReturn(Optional.of(emp));
		when(repository.save(updatedEmp)).thenReturn(updatedEmp);

		Employee employee = employeeRESTController.updateEmployee(updatedEmp, 1L);

		Assert.assertEquals(updatedEmp.toString(), employee.toString());
		verify(repository).save(updatedEmp);
	}

	@Test
	public void testUpdateEmployeeNotFound() {
		Employee updatedEmp = new Employee(1L, "newFirst", "newLast", "newEmail.com");

		when(repository.save(updatedEmp)).thenReturn(updatedEmp);

		Employee employee = employeeRESTController.updateEmployee(updatedEmp, 1L);

		Assert.assertEquals(updatedEmp.toString(), employee.toString());
		verify(repository).save(updatedEmp);
	}

	@Test
	public void testDeleteEmployee() {

		employeeRESTController.deleteEmployee(1L);

		verify(repository).deleteById(1L);
	}

	@Test
	public void testGetExternalUrl() {

	}
}
