package com.dental.lab.control;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.comparator.Comparators;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.payloads.ProductViewPayload;
import com.dental.lab.security.CustomUserDetails;
import com.dental.lab.services.OrderService;
import com.dental.lab.services.ProductCategoryService;
import com.dental.lab.services.ProductService;

@Controller
@RequestMapping(path = "/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService categoryService;
	
	@GetMapping(path = "/create")
	public ModelAndView goCreateOrder(
			@RequestParam(name = "category_id", required = false) Optional<Long> optCategoryId,
			ModelMap model) {
		
		CustomUserDetails principal = 
				(CustomUserDetails) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
		
		ProductCategory category;
		
		try {
			category = 
					categoryService.findById(optCategoryId.get());
		} catch(NoSuchElementException e) {
			category = categoryService.findRootCategory();
		}
		
		List<ProductCategory> subCategories = category.getSubCategories()
				.stream()
				.sorted(Comparator.comparing(c -> c.getName()))
				.collect(Collectors.toList());
		
		List<ProductViewPayload> productsById = 
				productService.findByCategoryId(category.getId())
				.stream()
				.map(product -> ProductViewPayload.build(product))
				.sorted(Comparator.comparing(pv -> pv.getName()))
				.collect(Collectors.toList());
		
		model.addAttribute("category", category);
		model.addAttribute("subCategories", subCategories);
		model.addAttribute("products", productsById);
		model.addAttribute("categoryPath", categoryService.findCategoryPath(category.getId()));
		model.addAttribute("userId", principal.getId());
		
		return new ModelAndView("/orders/create-order", model);
	}

}
