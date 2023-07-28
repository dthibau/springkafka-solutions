package org.formation.greetings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

	@Autowired
	GreetingsProperties props;
	
	@RequestMapping("/hello")
	public String hello(@RequestParam String name) {
		return props.getGreeting() + name;
	}
}
