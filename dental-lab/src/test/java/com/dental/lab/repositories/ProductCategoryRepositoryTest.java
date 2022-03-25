package com.dental.lab.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.dental.lab.model.entities.ProductCategory;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductCategoryRepositoryTest {
	
	@Autowired
	private TestEntityManager em;
	
	@Autowired
	private ProductCategoryRepository categoryRepo;
	
	@Test
	public void shouldFindRootCategories() {
		ProductCategory rootCategory = new ProductCategory();
		rootCategory.setName("root");
		rootCategory.setDepth(0);
		em.persist(rootCategory);
		
		ProductCategory category1 = new ProductCategory();
		category1.setName("category 1");
		category1.setDepth(1);
		em.persist(category1);
		
		rootCategory.getSubCategories().add(category1);
		category1.setParentCategory(rootCategory);
		em.persist(rootCategory);
		
		List<ProductCategory> rootCategories = categoryRepo.findRootCategories();
		
		assertThat(rootCategories)
			.extracting("name")
			.containsOnly("root", "Todos los productos");
		
	}

}
