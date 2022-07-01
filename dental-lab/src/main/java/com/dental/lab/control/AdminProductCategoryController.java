package com.dental.lab.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.services.ProductCategoryService;

@Controller
@RequestMapping(path = "/admin/categories")
public class AdminProductCategoryController {
	
	@Autowired
	private ProductCategoryService categoryService;
	
	@GetMapping(path = "/create")
	public ModelAndView goCreateCategory(ModelMap model) {
		
		return new ModelAndView("products/admin-create-category", model);
	}
	
	@PostMapping(path = "/create")
	public ModelAndView createCategory(
			@RequestParam("name") String name,
			@RequestParam(name = "parentCategoryId", required = false) Long parentCategoryId,
			ModelMap model) {
		
		categoryService.createProductCategory(parentCategoryId, name);
		
		return new ModelAndView("redirect:/admin/categories/create");
	}

}
