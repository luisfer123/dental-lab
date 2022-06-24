package com.dental.lab.control.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductRestController {
	
	@PutMapping(path = "/picture")
	public ResponseEntity<?> updatePicture(
			@RequestParam("newPicture") byte[] newImage) {
		
		System.out.println("updateImage executed");
		
		return ResponseEntity.ok().build();
	}

}
