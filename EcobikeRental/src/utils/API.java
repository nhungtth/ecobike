package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import entity.transaction.CreditCard;

/**
 * Class cung cap cac phuong thuc gui request len server va nhan du lieu tra ve
 * Date 10/12/2021
 * @author NhungTTH
 * @version 1.0
 */
public class API {

	/**
	 * Thuoc tinh giup format ngay thang theo dinh dang
	 */
	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/**
	 * Thuoc tinh giup log thong tin ra console
	 */
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());

	/**
	 * Phuong thuc giup goi cac api dang GET
	 * @param url: duong dan toi server can request
	 * @param token: doan ma bam can cung cap de xac thuc nguoi dung
	 * @return response: phan hoi tu server (dang string)
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception {
		// phan 1: setup
		HttpURLConnection conn = setupConnection(token, url, "GET");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		
		// phan 2: doc du lieu tra ve tu server
		String response = readResponse(conn);
		return response;
	}

	/**
	 * Phuong thuc doc du lieu tra ve tu server
	 * @param conn: ket noi toi server
	 * @return 
	 * @throws IOException
	 */
	private static String readResponse(HttpURLConnection conn) throws IOException {
		String inputLine;
		BufferedReader in;

		if(conn.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		
		StringBuilder response = new StringBuilder(); 
		while ((inputLine = in.readLine()) != null)
			System.out.println(inputLine);
		response.append(inputLine + "\n");
		in.close();
		LOGGER.info("Respone Info: " + response.substring(0, response.length() - 1).toString());
		return response.substring(0, response.length() - 1).toString();
	}

	/**
	 * Thiet lap connection toi server
	 * @param token: doan ma bam can cung cap de xac thuc nguoi dung
	 * @param url: duong dan toi server can request
	 * @param method: giao thuc api
	 * @return connection
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private static HttpURLConnection setupConnection(String token, String url, String method)
			throws MalformedURLException, IOException, ProtocolException {
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		
		return conn;
	}

	int var;

	/**
	 * Phuong thuc giup cac api dang POST (thanh toan,...)
	 * @param url: duong dan toi server can request
	 * @param data: du lieu dua len server de xu ly (dang JSON)
	 * @return response: phan hoi tu server (dang string)
	 * @throws IOException
	 */
	public static String post(String url, String data
//	       , String token
	   ) throws IOException {
	      allowMethods("PATCH");
	      URL line_api_url = new URL(url);
	      String payload = data;
	      LOGGER.info("Request Info:\nRequest URL: " + url + "\n" + "Payload Data: " + payload + "\n");
	      HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
	      conn.setDoInput(true);
	      conn.setDoOutput(true);
	      conn.setRequestMethod("PATCH");
	      conn.setRequestProperty("Content-Type", "application/json");
//	    conn.setRequestProperty("Authorization", "Bearer " + token);
	      Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	      writer.write(payload);
	      writer.close();
	      BufferedReader in;
	      String inputLine;
	      if (conn.getResponseCode() / 100 == 2) {
	         in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      } else {
	         in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	      }
	      StringBuilder response = new StringBuilder();
	      while ((inputLine = in.readLine()) != null)
	         response.append(inputLine);
	      in.close();
	      LOGGER.info("Respone Info: " + response.toString());
	      return response.toString();
	   }

	/**
	 * Phuong thuc cho phep goi cac loai giao thuc API khac nhau nhu PUT, PATCH ... (chi hoat dong voi Java 11)
	 * @deprecated chi hoat dong voi Java <= 11
	 * @param methods: giao thuc can cho phep (PATCH, PUT,...)
	 */
	private static void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
