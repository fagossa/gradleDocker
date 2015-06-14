package com.docker.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

	public static String ESurl() {
		String host = DEFAULT_HOST;
		final Runtime rt = Runtime.getRuntime();
		final String[] commands = {"boot2docker", "ip"};
		try {
			final Process proc = rt.exec(commands);
			final BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(proc.getInputStream()));
			String response = stdInput.readLine();
			if (response != null) {
				host = response;
			}
		} catch (IOException e) {
			// ignoring error
		}
		return composeUrl(host, ES_PORT);
	}

	protected static void waitForESAvailability(int retry) {
		if (retry <= 0) {
			return;
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(ESurl()).openConnection();
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				System.out.println("====> NOT OK " + responseCode);
				//waitForESAvailability(retry - 1);
			}
		} catch (Exception e) {
			System.out.println("...waiting for ES");
			try { Thread.sleep(500); } catch (InterruptedException e1) {}
			waitForESAvailability(retry - 1);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
