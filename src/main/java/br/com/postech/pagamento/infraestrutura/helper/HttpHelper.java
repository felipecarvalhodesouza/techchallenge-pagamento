package br.com.postech.pagamento.infraestrutura.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    private final String API_URL = "http://ad1b7891a07a84b3aae6d178d1cc98db-1751645578.us-east-1.elb.amazonaws.com:8083/";
    
    public String sendPostRequest(String requestBody) throws IOException {
    	return sendPostRequest(requestBody, (HttpURLConnection) new URL(API_URL).openConnection());
    }
    
    public String sendPostRequest(String requestBody, URL url) throws IOException {
    	return sendPostRequest(requestBody, (HttpURLConnection) url.openConnection());
    }

    public String sendPostRequest(String requestBody, HttpURLConnection connection) throws IOException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
}