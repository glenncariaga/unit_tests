package com.nlg.oneview;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nlg.oneview.controller.EmployeeRESTController;
import com.nlg.oneview.model.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneViewDataApiApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(OneViewDataApiApplicationTests.class);

	@Autowired
	private EmployeeRESTController employeeRESTController;

	@Before
	public void init() {
	}

	@Test
	public void createNewEmployeeFromExternalApi() throws Exception {
		// mock data
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");

		Employee employee = employeeRESTController.externalCall(emp);
		Assert.assertEquals(emp.toString(), employee.toString());
	}
}
