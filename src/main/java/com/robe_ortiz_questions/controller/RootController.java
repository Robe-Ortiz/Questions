package com.robe_ortiz_questions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	

}
