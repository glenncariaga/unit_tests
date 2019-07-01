package com.nlg.oneview;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nlg.oneview.controller.EmployeeRESTController;
import com.nlg.oneview.model.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneViewDataApiApplicationTests {

	@Autowired
	private EmployeeRESTController employeeRESTController;

	String url = "http://localhost:8090/employee-management/employees";

	@Before
	public void init() {

	}

	@Test
	public void doesExternalCallMakeExternalCall() throws Exception {
		// mock data
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");

		Employee employee = employeeRESTController.externalCall(emp);
		Assert.assertEquals(emp.toString(), employee.toString());
	}

	@PrepareForTest({ URL.class, EmployeeRESTController.class, Employee.class, HttpURLConnection.class })
	@Test
	public void doesExternalCallUseTheHttpConnector() throws Exception {
		// mock data
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");
		InputStream inputStream = new ByteArrayInputStream(emp.toString().getBytes(StandardCharsets.UTF_8));

		URL mockURL = PowerMockito.mock(URL.class);
		HttpURLConnection mockConnection = PowerMockito.mock(HttpURLConnection.class);

		PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(mockURL);
		PowerMockito.when(mockURL.openConnection()).thenReturn(mockConnection);
		PowerMockito.when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
		PowerMockito.when(mockConnection.getInputStream()).thenReturn(inputStream);

		Employee employee = employeeRESTController.externalCall(emp);
		Assert.assertEquals(emp.toString(), employee.toString());

		Mockito.verify(mockConnection).setRequestMethod("POST");
		Mockito.verify(mockConnection).setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	}
}
