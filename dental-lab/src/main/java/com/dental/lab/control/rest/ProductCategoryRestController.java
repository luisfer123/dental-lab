package com.dental.lab.control.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dental.lab.exceptions.ProductCategoryNotFoundException;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.payloads.ParentChildrenProductCategoryPayload;
import com.dental.lab.services.ProductCategoryService;

@RestController
@RequestMapping(path = "/api/v1/categories")
public class ProductCategoryRestController {
	
	@Autowired
	private ProductCategoryService categoryService;
	
	@GetMapping(path = "/root")
	public ResponseEntity<?> getRootCategory() {
		
		ParentChildrenProductCategoryPayload response = null;
		
		try {
			ProductCategory rootCategory = 
					categoryService.findRootCategory();
			response = 
					categoryService.buildParentChildrenCategoryRestResponse(rootCategory.getId());
		} catch(Exception e) {
			ResponseEntity
				.badRequest()
				.build();
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(path = "/children")
	public ResponseEntity<?> getCategory(
			@RequestParam("parentCategoryId") Long categoryId) {
		
		ParentChildrenProductCategoryPayload response =
				new ParentChildrenProductCategoryPayload();
		
		try {
			response = categoryService
					.buildParentChildrenCategoryRestResponse(categoryId);
		} catch(Exception e) {
			return ResponseEntity
					.badRequest()
					.build();
		}
		
		return ResponseEntity
				.ok(response);
		
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<?> getCategoryById(
			@PathVariable("id") Long categoryId) {
		
		ProductCategory category = null;
		
		try {
			category = categoryService.findById(categoryId);
		} catch(ProductCategoryNotFoundException e) {
			return ResponseEntity
					.badRequest()
					.build();
		}
		
		return ResponseEntity
				.ok(category);
		
	}

}
