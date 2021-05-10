package com.jplunge.main.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class OktaLoginService {
	
	
	@Autowired
	RestTemplate restTemplate;

	
	private final Logger logger = LoggerFactory.getLogger(OktaLoginService.class);
	
	@Value("${okta.url.login}")
    private String urlOktaApiAuthn;
	@Value("${okta.user.name}")
    private String user;
    @Value("${okta.user.password}")
    private String password;
    
	public boolean oktaApiAuthenticationLogin() {
		boolean retval = false;
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("username", user);
        map.put("password", password);
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        // send POST request
        ResponseEntity<String> response = this.restTemplate.postForEntity(urlOktaApiAuthn, entity, String.class);
        logger.info(response.toString());
        if(response.toString().toUpperCase().contains("SUCCESS")) {
        	retval = true;
        }
		return retval;
	}
}
