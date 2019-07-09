package com.nlg.oneview;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlg.oneview.model.Employee;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneViewDataApiApplicationIntegrationTests {

	ObjectMapper object;

	String baseUrl = "http://localhost:8090/employee-management";
	String id;

	@Before
	public void init() throws IOException {
		// mock data
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");

		ObjectMapper _payload = new ObjectMapper();

		String payload = _payload.writeValueAsString(emp);
		String url = baseUrl + "/employees";

		// build the request
		okhttp3.RequestBody body = httpRequestBodyBuilder(payload);

		// request/response cycle
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = httpResponse(request);
		payload = response.body().string();

		emp = _payload.readValue(payload, Employee.class);

		id = emp.getId().toString();
	}

	@Test
	public void canCreateOrSaveEmployee() throws IOException {

		// mock data
		Employee emp = new Employee(1L, "First", "Last", "email@email.com");

		ObjectMapper _payload = new ObjectMapper();

		String payload = _payload.writeValueAsString(emp);
		String url = baseUrl + "/employees";

		// build the request
		okhttp3.RequestBody body = httpRequestBodyBuilder(payload);

		// request/response cycle
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = httpResponse(request);

		// test cases
		Assert.assertEquals(response.code(), 200);

	}

	@Test
	public void canGetAllEmployees() throws IOException {
		String url = baseUrl + "/employees";

		Request request = new Request.Builder().url(url).get().build();
		Response response = httpResponse(request);

		Assert.assertEquals(response.code(), 200);
	}

	@Test
	public void canGetEmployeeById() throws IOException {
		String url = baseUrl + "/employees/";

		Request request = new Request.Builder().url(url + id).get().build();
		Response response = httpResponse(request);

		Assert.assertEquals(response.code(), 200);
	}

	@Test
	public void canPutEmployee() throws IOException {
		String url = baseUrl + "/employees/";
		Employee emp = new Employee(1L, "FirstEdit", "LastEdit", "emailEdit@email.com");
		ObjectMapper _payload = new ObjectMapper();

		String payload = _payload.writeValueAsString(emp);

		// do the put request and record the response
		okhttp3.RequestBody body = httpRequestBodyBuilder(payload);
		Request putRequest = new Request.Builder().url(url + id).put(body).build();
		Response putResponse = httpResponse(putRequest);

		// get the same id from the endpoint
		Request getRequest = new Request.Builder().url(url + id).get().build();
		Response getResponse = httpResponse(getRequest);

		// test the put response body is the same as the get response body.
		Assert.assertEquals(getResponse.body().string(), putResponse.body().string());
		Assert.assertEquals(putResponse.code(), 200);
	}

	@Test
	public void canDeletePost() throws IOException {
		String url = baseUrl + "/employees/";

		Request request = new Request.Builder().url(url + id).delete().build();
		Response response = httpResponse(request);

		Assert.assertEquals(response.code(), 200);
	}

	@Test
	public void can404Error() throws IOException {
		String url = baseUrl + "/noUrl";
		Request request = new Request.Builder().url(url).get().build();
		Response response = httpResponse(request);

		Assert.assertEquals(response.code(), 404);
	}

	// JSON parser
	private okhttp3.RequestBody httpRequestBodyBuilder(String payload) {
		okhttp3.RequestBody body = okhttp3.RequestBody.create(payload,
				okhttp3.MediaType.parse("application/json; charset=utf-8"));
		return body;
	}

	// wrapper for okhttp response
	private Response httpResponse(Request request) throws IOException {
		OkHttpClient client = new OkHttpClient();

		Response response = client.newCall(request).execute();
		return response;
	}

}
