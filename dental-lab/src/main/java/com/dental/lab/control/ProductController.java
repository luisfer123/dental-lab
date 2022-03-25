package com.dental.lab.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.services.ProductService;

@Controller
@RequestMapping(path = "/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	/**
	 * 
	 * @param model
	 * @return - A list with all products currently stored in the database. If the list
	 * gets to large, {@code goAllProductsPaginated()} method should be used instead.
	 */
	@GetMapping(path = "/list")
	public ModelAndView goAllProducts(ModelMap model) {
		List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		
		List<ProductCategory> rootCategories = productService.findRoodCategories();
		model.addAttribute("categories", rootCategories);
		
		return new ModelAndView("products/products", model);
		
	}

}
