package com.docker.test;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class HelloService {

	private final String jsonRequest;
	private final RestApiConsumer consumer;
	private final String urlPrefix;

	public HelloService(String jsonRequest, String serviceAddress) throws Exception {
		Objects.requireNonNull(jsonRequest);
		Objects.requireNonNull(serviceAddress);

		consumer = new RestApiConsumer();
		consumer.execute(consumer.POST, serviceAddress + "/example/toto", jsonRequest);
		this.urlPrefix = serviceAddress;
		this.jsonRequest = jsonRequest;
	}

	String lastHello() throws Exception {
		final String query = "{\n" +
				"    \"query\": {\n" +
				"        \"query_string\": {\n" +
				"            \"query\": \"hello\"\n" +
				"        }\n" +
				"    }\n" +
				"}";
		consumer.execute(consumer.POST, urlPrefix + "/_search", query);
		String response = consumer.getResponse();
		return response != null ? response : "";
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("name", jsonRequest).
				toString();
	}

}
