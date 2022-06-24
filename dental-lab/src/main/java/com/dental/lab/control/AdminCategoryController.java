package com.dental.lab.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

	
	@GetMapping(path = "/create")
	public ModelAndView goCreateCategory() {
		
		return new ModelAndView("products/admin-create-category");
	}
	
}
