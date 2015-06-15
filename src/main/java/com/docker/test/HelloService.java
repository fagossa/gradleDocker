package com.docker.test;

import java.util.Objects;

import static com.docker.test.RestApiConsumer.POST;

public class HelloService {

	private final RestApiConsumer consumer;
	private final String urlPrefix;

	public HelloService(String serviceAddress) throws Exception {
		Objects.requireNonNull(serviceAddress);
		consumer = new RestApiConsumer();
		this.urlPrefix = serviceAddress;
	}

	public void sayHello(String jsonRequest) throws Exception {
		Objects.requireNonNull(jsonRequest);
		consumer.execute(POST, urlPrefix + "/example/toto", jsonRequest);
	}

	public String lastHello() {
		final String query = "{\n" +
				"    \"query\": {\n" +
				"        \"query_string\": {\n" +
				"            \"query\": \"hello\"\n" +
				"        }\n" +
				"    }\n" +
				"}";
		String response = "";
		try {
			consumer.execute(POST, urlPrefix + "/example/_search", query);
			response = consumer.getResponse();
		} catch (Exception e) {
			// ignore
		}
		return response != null ? response : "";

	}

}
