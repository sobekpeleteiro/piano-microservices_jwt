package com.jplunge.main.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jplunge.main.config.JwtTokenUtil;
import com.jplunge.main.model.JwtRequest;
import com.jplunge.main.model.JwtResponse;
import com.jplunge.main.service.JwtUserDetailsService;
import com.jplunge.main.service.OktaLoginService;
import com.jplunge.main.service.RestControllerPianoService;



/**
 * 
 * Expose a POST API /authenticate using the JwtAuthenticationController. 
 * The POST API gets username and password in the body- Using Spring Authentication Manager we authenticate the username and password.
 * If the credentials are valid, a JWT token is created using the JWTTokenUtil and provided to the client.
 * 
 * @author oscardelatorre
 *
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {
		
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OktaLoginService oktaLoginService;


	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		//this call has not been able to avoid with the user id found in application.properties so we just use it as a secret (user:javainuse / password (encrytpted)
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		//at this point the authentication manager in Spring has approved 
		//now we make a control to okta with rest template just as an extra Use Case to demonstrate a API login (Could not do it with AuthenticationProvider)
		if(oktaLoginService.oktaApiAuthenticationLogin()) {
			//here we start with our own token since Okta token has been a pain in the ass
			final UserDetails userDetails = userDetailsService
					.loadUserByUsername(authenticationRequest.getUsername());
			final String token = jwtTokenUtil.generateToken(userDetails);
	
			return ResponseEntity.ok(new JwtResponse(token));
		}else {
			return null;
		}
		
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	
}
