package com.dental.lab.services.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductPricing;
import com.dental.lab.repositories.ProductCategoryRepository;
import com.dental.lab.repositories.ProductPricingRepository;
import com.dental.lab.repositories.ProductRepository;
import com.dental.lab.services.ProductService;

@Service
public class DataInitializationServiceImpl {
	
	@Autowired
	private ProductPricingRepository pricingRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryRepository categoryRepo;
	
	@Transactional
	@EventListener(classes = ApplicationReadyEvent.class)
	public void onInit() {
			
		addProductPricing();
	}
	
	public void addProducts() {
		Product incrustacionDeResina = 
				new Product("Incrustación de Resina", "descritpion");
	}
	
	@Transactional
	public void addProductCategories() {
		
		if(categoryRepo.findAll().size() == 0) {
			
			ProductCategory rootCategory = 
					new ProductCategory("Categoría Raíz", 0, null);
			rootCategory = categoryRepo.save(rootCategory);
			
			ProductCategory resina = 
					new ProductCategory("Resina", 1, rootCategory);
			resina = categoryRepo.save(resina);
			
			ProductCategory ceramica =
					new ProductCategory("Cerámica", 1, rootCategory);
			ceramica = categoryRepo.save(ceramica);
			
			ProductCategory prostodonciaRemovible = 
					new ProductCategory("Prostodoncia Removible", 1, rootCategory);
			prostodonciaRemovible = categoryRepo.save(prostodonciaRemovible);
			
			ProductCategory metalCeramica =
					new ProductCategory("Metal Cerámica", 2, ceramica);
			metalCeramica = categoryRepo.save(metalCeramica);
			
			ProductCategory libreDeMetal =
					new ProductCategory("Libre de Metal", 2, ceramica);
			libreDeMetal = categoryRepo.save(libreDeMetal);
			
			ProductCategory totales =
					new ProductCategory("Totales", 2, prostodonciaRemovible);
			totales = categoryRepo.save(totales);
			
			ProductCategory parciales =
					new ProductCategory("Parciales", 2, prostodonciaRemovible);
			parciales = categoryRepo.save(parciales);
			
		}
	}
	
	@Transactional
	public void addProductPricing() {
		
		if(pricingRepo.findAll().size() == 0) {
			
			int increment = 1;
			List<Product> products = productRepo.findAll();
			
			for(Product product: products) {
				ProductPricing price1 = new ProductPricing();
				price1.setCreationDate(new Timestamp(System.currentTimeMillis() + 1000*increment));
				price1.setPrice(BigDecimal.valueOf(increment + 100));
				
				ProductPricing price2 = new ProductPricing();
				price2.setCreationDate(new Timestamp(System.currentTimeMillis() + 1000*increment));
				price2.setPrice(BigDecimal.valueOf(increment + 100));
											
				ProductPricing price3 = new ProductPricing();
				price3.setCreationDate(new Timestamp(System.currentTimeMillis() + 1000*increment));
				price3.setPrice(BigDecimal.valueOf(increment + 100));
				
				product.addPrice(price1);
				product.addPrice(price2);
				product.addPrice(price3);
				
				increment += 1;
				
				productRepo.save(product);
			}
		}
	}
	
	public Product addProductCategories(ProductCategory category, Product product) {
		if(category != null)
			product.addProductCategory(category);
		
		while(category.getParentCategory() != null) {
			category = category.getParentCategory();
			product.addProductCategory(category);
		}
		
		return product;
	}
	
	public Long daysToMiliseconds(int numOfDays) {
		return Long.valueOf(numOfDays * 24 * 60 * 60 * 1000);
	}

}
