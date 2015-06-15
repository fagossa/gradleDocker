package com.docker.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.docker.test.RestApiConsumer.POST;

public class ESTestRegistry {

	protected static final String DEFAULT_HOST = "localhost";
	public static final int ES_PORT = 9200;

	public static String url(int port) throws IOException, InterruptedException {
		final Runtime rt = Runtime.getRuntime();
		final Process pr = rt.exec("boot2docker ip");
		int retVal = pr.waitFor();
		if (retVal == 0) {
			final String parseIpResponse = parseIpResponse(pr.getInputStream());
			return composeUrl(parseIpResponse, port);
		} else { //boot2docker not available
			return DEFAULT_HOST;
		}
	}

	private static String parseIpResponse(final InputStream p) throws IOException {
		final BufferedReader input = new BufferedReader(new InputStreamReader(p));
		final String line;
		try {
			while ((line = input.readLine()) != null) {
				return line;
			}
		} finally {
			p.close();
		}
		return DEFAULT_HOST;
	}

	private static String composeUrl(String host, int port) {
		return "http://" + host + ":" + port;
	}

	public String elasticSearchURL() {
		String host = DEFAULT_HOST;
		final Runtime rt = Runtime.getRuntime();
		try {
			final Process proc = rt.exec("boot2docker ip");
			int retVal = proc.waitFor();
			final BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(proc.getInputStream()));
			final String response = stdInput.readLine();
			if (response != null) {
				host = response;
			}
		} catch (Exception e) {
			// ignoring error
			e.printStackTrace();
		}
		return composeUrl(host, ES_PORT);
	}

	protected void waitForESAvailability(int retry) {
		if (retry <= 0) {
			return;
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(elasticSearchURL()).openConnection();
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				System.out.println("====> Elasticsearch is available :)");
			}
		} catch (Exception e) {
			System.out.println("...waiting for ES, error: " + e.getMessage());
			try { Thread.sleep(500); } catch (InterruptedException e1) {}
			waitForESAvailability(retry - 1);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	protected void refreshIndex(String index) {
		final RestApiConsumer consumer = new RestApiConsumer();
		try {
			consumer.execute(POST, elasticSearchURL() + "/" + index + "/_refresh", "");
		} catch (Exception e) {}
	}

}
