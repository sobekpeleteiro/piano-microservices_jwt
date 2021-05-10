package com.jplunge.main;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jplunge.main.service.OktaLoginService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class PianoMicroservicesJwtApplicationTests {

	@Autowired
	private OktaLoginService oktaLoginService;

	@Test
	void contextLoads() {
	}

	
	
	@Test
	void oktaLogin() {
		assertEquals(this.oktaLoginService.oktaApiAuthenticationLogin(), true);	
	    
	}
}

