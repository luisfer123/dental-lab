
package com.dental.lab.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@GetMapping(path = "/login")
	public ModelAndView goLoginForm() {
		return new ModelAndView("auth/login-page");
	}

}
