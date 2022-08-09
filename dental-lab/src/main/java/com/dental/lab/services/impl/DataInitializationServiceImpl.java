package com.dental.lab.services.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductPricing;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.repositories.AuthorityRepository;
import com.dental.lab.repositories.ProductCategoryRepository;
import com.dental.lab.repositories.ProductPricingRepository;
import com.dental.lab.repositories.ProductRepository;
import com.dental.lab.repositories.UserRepository;

@Service
public class DataInitializationServiceImpl {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AuthorityRepository authRepo;
	
	@Autowired
	private ProductPricingRepository pricingRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductCategoryRepository categoryRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Transactional
	@EventListener(classes = ApplicationReadyEvent.class)
	public void onInit() {
		
		addAuthoritiesAndUsers();
		addProductCategories();
		addProductsWithCategories();
		addProductPricing();
	}
	
	@Transactional
	public void addAuthoritiesAndUsers() {
		if(userRepo.findAll().size() == 0 && authRepo.findAll().size() == 0) {
			
			Authority roleAdmin = new Authority(EAuthority.ROLE_ADMIN);
			Authority roleClient = new Authority(EAuthority.ROLE_CLIENT);
			Authority roleDentist = new Authority(EAuthority.ROLE_DENTIST);
			Authority roleTechnician = new Authority(EAuthority.ROLE_TECHNICIAN);
			Authority roleUser = new Authority(EAuthority.ROLE_USER);
			
			roleAdmin = authRepo.save(roleAdmin);
			roleClient = authRepo.save(roleClient);
			roleDentist = authRepo.save(roleDentist);
			roleTechnician = authRepo.save(roleTechnician);
			roleUser = authRepo.save(roleUser);
			
			User adminUser = new User(
					"admin", encoder.encode("password"), "admin@mail.com", "adminn", "adminsn", "adminap", "adminam");
			adminUser.addAuthority(roleAdmin);
			adminUser.addAuthority(roleClient);
			adminUser.addAuthority(roleDentist);
			adminUser.addAuthority(roleTechnician);
			adminUser.addAuthority(roleUser);
			userRepo.save(adminUser);
			
			User clientUser = new User(
					"client", encoder.encode("password"), "client@mail.com", "clientn", "clientsn", "clientap", "clientam");
			clientUser.addAuthority(roleClient);
			clientUser.addAuthority(roleUser);
			userRepo.save(clientUser);
			
			User dentistUser = new User(
					"dentist", encoder.encode("password"), "dentist@mail.com", "dentistn", "dentistsn", "dentistap", "dentistam");
			dentistUser.addAuthority(roleDentist);
			dentistUser.addAuthority(roleUser);
			userRepo.save(dentistUser);
			
			User techUser = new User(
					"tech", encoder.encode("password"), "tech@mail.com", "techn", "techsn", "techap", "techam");
			techUser.addAuthority(roleTechnician);
			techUser.addAuthority(roleUser);
			userRepo.save(techUser);
			
			User user = new User(
					"user", encoder.encode("password"), "user@mail.com", "usern", "usersn", "userap", "useram");
			user.addAuthority(roleUser);
			userRepo.save(user);
			
		}
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
	public void addProductsWithCategories() {
		
		if(productRepo.findAll().size() == 0) {
			
			ProductCategory resina = categoryRepo.findByName("Resina").get();
			ProductCategory metalCeramica = categoryRepo.findByName("Metal Cerámica").get();
			ProductCategory libreDeMetal = categoryRepo.findByName("Libre de Metal").get();
			
			Product incrustacionDeResina =
					new Product("Incrustación de Resina", "descritpion");
			incrustacionDeResina = 
					this.addCategoryPathToProduct(resina, incrustacionDeResina);
			productRepo.save(incrustacionDeResina);
			
			Product carillaDeResina = 
					new Product("Carilla de Resina", "description");
			carillaDeResina = 
					this.addCategoryPathToProduct(resina, carillaDeResina);
			productRepo.save(carillaDeResina);
			
			Product coronaDeResina = 
					new Product("Corona de Resina", "description");
			coronaDeResina = 
					this.addCategoryPathToProduct(resina, coronaDeResina);
			productRepo.save(coronaDeResina);
			
			Product unidadMetalCeramica =
					new Product("Unidad Metal Cerámica", "description");
			unidadMetalCeramica = 
					this.addCategoryPathToProduct(metalCeramica, unidadMetalCeramica);
			productRepo.save(unidadMetalCeramica);
			
			Product metalCeramicaConCollarCeramico = 
					new Product("Metal Cerámica con Collar Cerámico", "descritpion");
			metalCeramicaConCollarCeramico = 
					this.addCategoryPathToProduct(metalCeramica, metalCeramicaConCollarCeramico);
			productRepo.save(metalCeramicaConCollarCeramico);
			
			Product coronaDeDisilicatoDeLitio =
					new Product("Corona de Disilicato de Litio", "description");
			coronaDeDisilicatoDeLitio = 
					this.addCategoryPathToProduct(libreDeMetal, coronaDeDisilicatoDeLitio);
			productRepo.save(coronaDeDisilicatoDeLitio);
			
			Product carillaDeDisilicatoDeLitio =
					new Product("Carilla de Disilicato de Litio", "description");
			carillaDeDisilicatoDeLitio = 
					this.addCategoryPathToProduct(libreDeMetal, carillaDeDisilicatoDeLitio);
			productRepo.save(carillaDeDisilicatoDeLitio);
			
			Product incrustacionDeDisilicatoDeLitio =
					new Product("Incrustación de Disilicato de Litio", "description");
			incrustacionDeDisilicatoDeLitio = 
					this.addCategoryPathToProduct(libreDeMetal, incrustacionDeDisilicatoDeLitio);
			productRepo.save(incrustacionDeDisilicatoDeLitio);
			
			Product estratificadoSobreDisilicato =
					new Product("Estratificado Sobre Disilicato", "description");
			estratificadoSobreDisilicato = 
					this.addCategoryPathToProduct(libreDeMetal, estratificadoSobreDisilicato);
			productRepo.save(estratificadoSobreDisilicato);
			
			Product estratificadoSobreZirconia = 
					new Product("Estratificado Sobre Zirconia", "description");
			estratificadoSobreZirconia = 
					this.addCategoryPathToProduct(libreDeMetal, estratificadoSobreZirconia);
			productRepo.save(estratificadoSobreZirconia);
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
	
	@Transactional
	public Product addCategoryPathToProduct(ProductCategory category, Product product) {
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
