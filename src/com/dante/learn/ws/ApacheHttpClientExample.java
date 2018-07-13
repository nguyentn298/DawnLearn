package com.dante.learn.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

/**
 * Apache HttpClient can be used to send HTTP requests from client code to server. (or POSTMAN)
 */
public class ApacheHttpClientExample {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String GET_URL = "https://www.google.com/";

	private static final String POST_URL = "http://localhost:9090/SpringMVCExample/home";

	public static void main(String[] args) throws IOException {
		sendGET();
		System.out.println("GET DONE");
		sendPOST();
		System.out.println("POST DONE");
	}

	/**
	 *	Experiment 
	 */
	public void testCookie() throws ClientProtocolException, IOException {
		
		// Store each SIM instance's cookie in a separate HttpContext that allow it to have it's own credentials
		HttpContext httpContext = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
			connectionManager.setMaxTotal(2);
			connectionManager.setDefaultMaxPerRoute(3);
		HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager).setConnectionManagerShared(true);
		
		/**
		 * OR: CloseableHttpClient httpClient = HttpClients.createDefault();
		 */
		CloseableHttpClient httpClient = httpClientBuilder.build();
//		CloseableHttpClient httpClient = null;
		HttpGet httpGet = new HttpGet("http://localhost:8080/abc");
		// Get content here
		CloseableHttpResponse response = httpClient.execute(httpGet, httpContext);
	}
	private static void sendGET() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(GET_URL);
		httpGet.addHeader("User-Agent", USER_AGENT);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

		System.out.println("GET Response Status:: " + httpResponse.getStatusLine().getStatusCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();

		// print result
		System.out.println(response.toString());
		httpClient.close();
	}

	private static void sendPOST() throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL);
		httpPost.addHeader("User-Agent", USER_AGENT);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("userName", "Pankaj Kumar"));

		HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
		httpPost.setEntity(postParams);

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

		System.out.println("POST Response Status:: " + httpResponse.getStatusLine().getStatusCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();

		// print result
		System.out.println(response.toString());
		httpClient.close();

	}
	
	public String getContent(CloseableHttpResponse response) {
		InputStream inputStream = null;
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				inputStream = entity.getContent();
				String data = getFileContent(inputStream);
				return data;
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to get content of HttpResponse", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return null;
	}
	
	public static String getFileContent(InputStream inputStream) throws IOException {
		StringBuffer fileContent = new StringBuffer();
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		while ((line = in.readLine()) != null) {
			if(fileContent.length() > 0){
				fileContent.append(LINE_SEPARATOR);
			}
			fileContent.append(line);
		}
		return fileContent.toString();
	}

}
