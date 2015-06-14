package com.docker.test;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class HelloServiceTest extends ESTestRegistry {

	@Before
	public void onlyOnce() {
		waitForESAvailability(5);
	}

	@Test
	public void should_insert_msg_in_elasticsearch() throws Exception {
		// given
		final String jsonRequest = "{\"say\":\"hello\",\"to\":\"world\"}";

		// when
		final HelloService service = new HelloService(jsonRequest, ESurl());
		String response = service.lastHello();

		// then
		assertTrue(response.contains("world"));
	}

}
