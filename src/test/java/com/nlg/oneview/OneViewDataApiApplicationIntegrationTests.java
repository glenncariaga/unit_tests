package com.nlg.oneview;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
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
		Request request = httpRequestBuilder(url, body);
		Response response = httpResponse(request);

		// test cases
		Assert.assertEquals(response.code(), 200);

		// clean up
		String recievedResponse = response.body().string();
		ObjectMapper obj = new ObjectMapper();
		Employee employee = obj.readValue(recievedResponse, Employee.class);

		Request cleanupRequest = new Request.Builder().url(baseUrl + "/employees/" + employee.getId()).delete().build();

		Response cleanUpResponse = httpResponse(cleanupRequest);

		Assert.assertEquals(cleanUpResponse.code(), 200);
	}

	// JSON parser
	private okhttp3.RequestBody httpRequestBodyBuilder(String payload) {
		okhttp3.RequestBody body = okhttp3.RequestBody.create(payload,
				okhttp3.MediaType.parse("application/json; charset=utf-8"));
		return body;
	}

	// wrapper for okhttp request
	private Request httpRequestBuilder(String url, @Nullable okhttp3.RequestBody body) {

		Request request = new Request.Builder().url(url).post(body).build();
		return request;
	}

	// wrapper for okhttp response
	private Response httpResponse(Request request) throws IOException {
		OkHttpClient client = new OkHttpClient();

		Response response = client.newCall(request).execute();
		return response;
	}
}
