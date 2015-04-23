package com.docker.test;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class HelloService {

	private final String jsonRequest;
    final RestApiConsumer consumer;
    final String urlPrefix;

	public HelloService(String jsonRequest, String urlPrefix) throws Exception {
        consumer = new RestApiConsumer();
        this.urlPrefix = urlPrefix;
        consumer.execute(consumer.POST, urlPrefix + "/example/toto", jsonRequest);
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
        return consumer.getResponse();
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("name", jsonRequest).
				toString();
	}

}
