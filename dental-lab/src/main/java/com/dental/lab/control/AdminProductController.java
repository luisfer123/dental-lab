package com.dental.lab.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.exceptions.ProductNotFoundException;
import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.services.ProductCategoryService;
import com.dental.lab.services.ProductService;

@Controller
@RequestMapping(path = "/admin/products")
public class AdminProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService categoryService;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@GetMapping(path = "")
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
		
		return new ModelAndView("products/admin-products", model);
		
	}
	
	@GetMapping(path = "/create")
	public ModelAndView goCreateProduct() {
		
		return new ModelAndView("products/admin-create-product");
	}
	
	@GetMapping(path = "/edit")
	public ModelAndView goEditProduct(
			@RequestParam("product_id") Optional<Long> optProductId,
			ModelMap model) {
		
		Long productId = null;
		Product product = null;
		String productPicture = null;
		
		try {
			productId = optProductId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/products");
		}
		
		try {
			product = productService.findByIdWithCategories(productId);
		} catch (ProductNotFoundException e) {
			return new ModelAndView("redirect:/admin/products");
		}
		
		List<ProductCategory> sortedCategories = product.getCategories()
				.stream()
				.sorted(Comparator.comparingInt(category -> category.getDepth()))
				.collect(Collectors.toList());
		
		if(product.getPicture() != null && product.getPicture().length > 0) {
			productPicture = 
					Base64.getEncoder().encodeToString(product.getPicture());
		} else {
			try {
				Resource imageResource = 
						resourceLoader.getResource("classpath:static/images/No-photo-product.jpg");
				byte[] image = Files.readAllBytes(Paths.get(imageResource.getURI()));
				productPicture = Base64.getEncoder().encodeToString(image);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
						
		model.addAttribute("productId", productId);
		model.addAttribute("product", product);
		model.addAttribute("productCategories", sortedCategories);
		model.addAttribute("productPicture", productPicture);
		
		return new ModelAndView("products/admin-edit-product", model);
	}
	
	@PostMapping(path = "/edit")
	public ModelAndView editProductInfo(
			@RequestParam("product_id") Optional<Long> optProductId,
			@RequestParam("name") Optional<String> optName,
			@RequestParam("description") Optional<String> optDescription,
			@RequestParam("price") Optional<Integer> optPrice,
			ModelMap model) {
		
		Long productId = null;
		String name = null;
		String description = optDescription.orElse("");
		int price = -1;
		
		try {
			productId = optProductId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/products");
		}
		
		try {
			name = optName.get();
			price = optPrice.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/products/edit?product_id=" + productId);
		}
		
		System.out.println(productId + name + description + price);
		
		return new ModelAndView("redirect:/admin/products/edit?product_id=" + productId);
	}
	
	@PostMapping(path = "/update-picture")
	public ModelAndView updateProductPicture(
			@RequestParam("product_id") Optional<Long> optProductId,
			@RequestParam(name = "updatePicture", required = false) MultipartFile multiparFilePicture,
			ModelMap model) {
		
		Long productId = null;
		byte[] updatePicture = null;
		
		try {
			productId = optProductId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/products");
		}
		
		try {
			updatePicture = 
					multiparFilePicture.getBytes();
		} catch(IOException e) {
			// TODO add error message and go back to edit product page.
			return new ModelAndView("products/admin-edit-product", model);
		}
		
		productService.updateImage(productId, updatePicture);
		
		return new ModelAndView("redirect:/admin/products/edit?product_id=" + productId);
	}
	
	@PostMapping(path = "/category", produces = "application/json")
	public @ResponseBody String changeProductCategory(
			@RequestParam("productId") Optional<Long> optProductId,
			@RequestParam("parentCategoryId") Optional<Long> optCategoryId,
			ModelMap model) {
		
		Long productId = optProductId.get();
		Long categoryId = optCategoryId.get();
		
		productService.changeProductCategory(productId, categoryId);
		
		return "{\"msg\":\"success\"}";
	}
}
