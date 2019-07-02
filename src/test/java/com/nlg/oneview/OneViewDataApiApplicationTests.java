package com.nlg.oneview;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OneViewDataApiApplicationTests {

	private MockWebServer externalAPI;
	private String baseUrl;
	@Mock
	EmployeeRepository repository;

	@Autowired
	@InjectMocks
	private EmployeeRESTController employeeRESTController;

	@Before
	public void init() throws IOException {
		externalAPI = new MockWebServer();
		externalAPI.start();
		HttpUrl _baseUrl = externalAPI.url("/");
		baseUrl = _baseUrl.toString();

		MockitoAnnotations.initMocks(this);
		employeeRESTController = new EmployeeRESTController();
		employeeRESTController.setRepository(repository);
	}

	@After
	public void shutdown() throws IOException {
//		externalAPI.shutdown();
	}

	@Test
	public void doesExternalCallMakeExternalCall() throws Exception {
		// mock data
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");

		Employee employee = employeeRESTController.externalCall(emp, null);
		Assert.assertEquals(emp.toString(), employee.toString());
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

//		EmployeeRESTController mockedEndpoint = Mockito.mock(EmployeeRESTController.class);

		// run the method under test
//		Mockito.when(employeeRESTController.getExternalUrl()).thenReturn(baseUrl);
		Employee employee = employeeRESTController.externalCall(emp, baseUrl);

//		System.out.println("baseUrl: " + baseUrl);
//		System.out.println("check payload" + payload);
		// test cases
		Assert.assertEquals(emp.toString(), employee.toString());
	}
}
