package com.dental.lab.control;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.services.ProductCategoryService;
import com.dental.lab.services.ProductService;

@Controller
@RequestMapping(path = "/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService categoryService;
	
	@GetMapping(path = "/list")
	public ModelAndView goAllProductsWithCategory(
			@RequestParam("category_id") Optional<Long> optCategoryId,
			ModelMap model) {
		
		if(!optCategoryId.isPresent()) {
			ProductCategory rootCategory = categoryService.findRoodCategory();
			model.addAttribute("products", productService.findAll());
			model.addAttribute("categories", categoryService.findSubCategories(rootCategory.getId()));
			model.addAttribute("categoryPath", categoryService.findCategoryPath(rootCategory.getId()));
		} else {
			Long categoryId = optCategoryId.get();
			model.addAttribute("products", productService.findByCategoryId(categoryId));
			model.addAttribute("categories", categoryService.findSubCategories(categoryId));
			model.addAttribute("categoryPath", categoryService.findCategoryPath(categoryId));
		}
		
		return new ModelAndView("products/products", model);
	}

}
