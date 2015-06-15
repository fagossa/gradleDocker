package com.docker.test;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class HelloServiceTest extends ESTestRegistry {

	@Before
	public void onlyOnce() {
		waitForESAvailability(15);
	}

	@Test
	public void should_insert_msg_in_elasticsearch() throws Exception {
		// given
		final String jsonRequest = "{\"say\":\"hello\",\"to\":\"world\"}";

		// when
		final HelloService service = new HelloService(elasticSearchURL());
		service.sayHello(jsonRequest);

		// then
		refreshIndex("example");
		final String response = service.lastHello();

		assertTrue(response.contains("world"));
	}

}
