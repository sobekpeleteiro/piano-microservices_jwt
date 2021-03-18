package com.jplunge.main;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@RequestMapping({ "/helloWorld" })
	public String firstPage() {
		return "Hello World";
	}

}