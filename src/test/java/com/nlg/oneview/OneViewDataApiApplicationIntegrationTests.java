package com.nlg.oneview;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlg.oneview.model.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OneViewDataApiApplicationIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void canCreateOrSaveEmployee() throws Exception {
		Employee emp = new Employee(5L, "First", "Last", "email@email.com");

		ObjectMapper obj = new ObjectMapper();
		String payload = obj.writeValueAsString(emp);

		this.mockMvc.perform(post("/employee-management/employees/").contentType("application/json").content(payload))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("First"));

	}

	@Test
	public void canGetAllEmployees() throws Exception {

		this.mockMvc.perform(get("/employee-management/employees/")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	public void canGetEmployeeById() throws Exception {
		this.mockMvc.perform(get("/employee-management/employees/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Vijay"));
	}

	@Test
	public void canPutEmployee() throws Exception {
		Employee emp = new Employee(5L, "First", "Last", "email@email.com");

		ObjectMapper obj = new ObjectMapper();
		String payload = obj.writeValueAsString(emp);

		this.mockMvc.perform(put("/employee-management/employees/2").contentType("application/json").content(payload))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("First"));
	}

	@Test
	public void canDeletePost() throws Exception {
		this.mockMvc.perform(delete("/employee-management/employees/3")).andExpect(status().isOk());

	}

	@Test
	public void can404Error() throws Exception {
		this.mockMvc.perform(get("/notRealUrl")).andExpect(status().is(404));
	}

	@Test
	public void cannotFindRecord() throws Exception {
		this.mockMvc.perform(get("/employee-management/employees/999"))
				.andExpect(jsonPath("$.message").value("INCORRECT_REQUEST"))
				.andExpect(jsonPath("$.details").value("Employee id '999' does no exist"));
	}

}
