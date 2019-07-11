package com.nlg.oneview;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.WebRequest;

import com.nlg.oneview.exception.CustomExceptionHandler;
import com.nlg.oneview.exception.RecordNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomExceptionHandlerUnitTest {
	CustomExceptionHandler handlerEx;
	@Mock
	WebRequest webRequest;
	@Mock
	RecordNotFoundException ex;

	@Test
	public void handleUserNotFoundExceptionReturnsError() {
//		ex.ResponseEntity response = handlerEx.handleUserNotFoundException(ex, webRequest);

	}
}
