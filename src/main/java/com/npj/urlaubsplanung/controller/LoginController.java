
package com.npj.urlaubsplanung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
	@GetMapping
	public String loginForm() {
		return "login";
	}
}
