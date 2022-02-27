package com.dental.lab.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	@GetMapping(path = {"/home", "/", "index"})
	public ModelAndView homeController() {
		return new ModelAndView("home");
	}

}
