package org.novamedia.novamail.jobserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class NetworkUtils {

	public static String getUrlText(String url, Map<String, String> queryParams) throws IOException  {
		StringBuffer params = new StringBuffer("");
		for (String key : queryParams.keySet()) {
			params.append(key + "=" + queryParams.get(key) + "&");
		}
		if (params.length() > 0) {
			params.delete(params.length() - 1, params.length());
		}
		return getUrlText(url + "?" + params.toString());
	}
	
	public static String getUrlText(String url) throws IOException  {
		System.out.println("entrance:" + url + "");
		URL website = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) website.openConnection();
		connection.setRequestProperty("Access-Control-Allow-Origin", "*");
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);

		in.close();
		System.out.println("exit:" + new Date() + " (" + new Date().getTime() + ")");

		return response.toString();
	}

	public static String getMethodUrlText(TreeMap<String, String> requestMap, String body, String httpMethod, String httpUrl) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, IOException, Exception {
		System.out.println("entrance:" + httpMethod + ":" + httpUrl);
		System.out.println("body:" + body);
		URL url = new URL(httpUrl);
		String host = url.getHost();
		String httpEndpoint = url.getPath();
		StringBuffer queryParamsStr = new StringBuffer("");
		for (String queryParamName : requestMap.keySet()) {
			queryParamsStr.append(queryParamName + "=" + requestMap.get(queryParamName) + "&");
		}
		if (queryParamsStr.length() > 0) {
			queryParamsStr.delete(queryParamsStr.length() - 1, queryParamsStr.length());
		}

		URL website = new URL(httpUrl + (queryParamsStr.length() > 0 ? "?" + queryParamsStr.toString() : ""));
		HttpURLConnection connection = (HttpURLConnection) website.openConnection();
		connection.setRequestProperty("content-Type", "application/json");
		connection.setRequestProperty("accept", "application/json");
		connection.setRequestProperty("host", host);
		switch (httpMethod) {
		case "GET":
			break;
		case "POST":
			break;
		case "PATCH":
			connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			connection.setRequestMethod("POST");
			break;
		case "DELETE":
			connection.setRequestMethod("DELETE");
			break;
		case "PUT":
			connection.setRequestMethod("PUT");
			break;
		}
		if (body != null) {
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			os.write( body.getBytes("UTF-8") );
			os.close();
		}
		Integer responseCode = connection.getResponseCode();
		if (responseCode != 200) {
			System.out.println("incorrect response code:" + responseCode);
			throw new Exception("" + responseCode);
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

		StringBuilder response = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		System.out.println("response.toString():" + response.toString());
		System.out.println("exit:" + new Date() + " (" + new Date().getTime() + ")");

		return response.toString();
	}

}
