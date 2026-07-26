package com.davifaustino.musicstore.order;

import java.net.http.HttpClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTests {

	@LocalServerPort
	protected int port;

	protected HttpClient httpClient;
    
	protected HttpHeaders headers;

    @BeforeEach
    void setup() {
		httpClient = HttpClient.newHttpClient();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
    }

	protected void testLog(String message) {
		System.out.println("\u001b[33m" + message + "\u001b[0m");
	}
}
