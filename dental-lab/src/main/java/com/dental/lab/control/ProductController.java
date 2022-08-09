package com.dental.lab.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.model.entities.Product;
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
	
	
	public ModelAndView goAllProductsWithCategory(
			@RequestParam("category_id") Optional<Long> optCategoryId,
			ModelMap model) {
		
		if(!optCategoryId.isPresent()) {
			ProductCategory rootCategory = categoryService.findRootCategory();
			model.addAttribute("products", productService.findAll());
			model.addAttribute("categories", categoryService.findSubCategories(rootCategory.getId()));
			model.addAttribute("categoryPath", categoryService.findCategoryPath(rootCategory.getId()));
		} else {
			try {
				Long categoryId = optCategoryId.get();
				model.addAttribute("products", productService.findByCategoryId(categoryId));
				model.addAttribute("categories", categoryService.findSubCategories(categoryId));
				model.addAttribute("categoryPath", categoryService.findCategoryPath(categoryId));
			} catch(Exception e) {
				e.printStackTrace();
				return new ModelAndView("redirect:/prodcts");
			}
		}
		
		return new ModelAndView("products/products", model);
	}
	
	@GetMapping(path = "/list")
	public ModelAndView goPageProductsWithGivenCategory(
			@RequestParam("category_id") Optional<Long> optCategoryId,
			@RequestParam("page_num") Optional<Integer> optPageNum,
			@RequestParam("page_size") Optional<Integer> optPageSize,
			@RequestParam("sort_by") Optional<String> optSortBy,
			ModelMap model) {
		
		Long categoryId = optCategoryId.orElseGet(
				() -> categoryService.findRootCategory().getId());
		int pageNum = optPageNum.orElse(0);
		int pageSize = optPageSize.orElse(9);
		String sortBy = optSortBy.orElse("name");
		
		Page<Product> productsPage = 
				productService.findByCategoryId(categoryId, pageNum, pageSize, sortBy);
		
		model.addAttribute("currentCategoryId", categoryId);
		model.addAttribute("products", productsPage.getContent());
		model.addAttribute("categories", categoryService.findSubCategories(categoryId));
		model.addAttribute("categoryPath", categoryService.findCategoryPath(categoryId));
		
		int totalPages = productsPage.getTotalPages();
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", productsPage.getNumber());
		
		List<Integer> pageNumbers;
		if(totalPages > 0) {
			pageNumbers = IntStream.range(0, totalPages)
					.boxed()
					.collect(Collectors.toList());
		} else {
			pageNumbers = new ArrayList<>();
		}
		model.addAttribute("pageNumbers", pageNumbers);
		
		return new ModelAndView("products/products", model);
		
	}

}
