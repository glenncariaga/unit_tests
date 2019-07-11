package com.nlg.oneview;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlg.oneview.controller.EmployeeRESTController;
import com.nlg.oneview.model.Employee;
import com.nlg.oneview.repository.EmployeeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OneViewDataApiApplicationIntegrationTests {

	@InjectMocks
	private EmployeeRESTController employeeRESTController;

	@Mock
	EmployeeRepository repository;

	@InjectMocks
	private MockMvc mockMvc;

	@Test
	public void canCreateOrSaveEmployee() throws Exception {
		Employee emp = new Employee(5L, "First", "Last", "email@email.com");

		ObjectMapper obj = new ObjectMapper();
		String payload = obj.writeValueAsString(emp);

		when(repository.save(emp)).thenReturn(emp);
		this.mockMvc.perform(post("/employee-management/employees/").contentType("application/json").content(payload))
				.andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.firstName").value("First")).andDo(print());

	}

	@Test
	public void canGetAllEmployees() throws Exception {
		List<Employee> employees = new ArrayList<Employee>();

		employees.add(new Employee(0L, "First0", "Last0", "email0@email.com"));
		employees.add(new Employee(0L, "First1", "Last1", "email1@email.com"));
		employees.add(new Employee(0L, "First2", "Last2", "email2@email.com"));
		System.out.println("total employees " + employees.toString());
		when(repository.findAll()).thenReturn(employees);
		this.mockMvc.perform(get("/employee-management/employees/")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void canGetEmployeeById() throws Exception {

	}

	@Test
	public void canPutEmployee() throws IOException {

	}

	@Test
	public void canDeletePost() throws IOException {

	}

	@Test
	public void can404Error() throws IOException {

	}

	@Test
	public void cannotFindRecord() throws IOException {

	}

}
