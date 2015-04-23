package com.docker.test;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.fail;
import static com.docker.test.IntegrationTools.*;

public class HelloServiceTest {

	@Test
	public void testSayHello() throws Exception {
		// given
		final String jsonRequest = "{\"say\":\"hello\",\"to\":\"world\"}";

		// when
		final HelloService service = new HelloService(jsonRequest,
							url() );
		String response = service.lastHello();

		// then
		assertTrue(response.contains("\"world\""));
	}

}
