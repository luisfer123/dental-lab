package com.dental.lab.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

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
		
		ProductCategory rootCategory = categoryRepo.findRootCategory();
		
		assertThat(rootCategory.getName().equals("Todos los productos"));
		
	}

}
