package com.dental.lab.model.payloads;

import java.util.ArrayList;
import java.util.List;

import com.dental.lab.model.entities.ProductCategory;

public class ParentChildrenProductCategoryPayload {
	
	private ProductCategory parentCategory;
	
	private List<ProductCategory> parentCategoryPath;
	
	private List<ProductCategory> childrenCategories;
	
	public ParentChildrenProductCategoryPayload() {
		this.parentCategoryPath = new ArrayList<>();
		this.childrenCategories = new ArrayList<>();
	}

	public ProductCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(ProductCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	public List<ProductCategory> getParentCategoryPath() {
		return parentCategoryPath;
	}

	public void setParentCategoryPath(List<ProductCategory> parentCategoryPath) {
		this.parentCategoryPath = parentCategoryPath;
	}

	public List<ProductCategory> getChildrenCategories() {
		return childrenCategories;
	}

	public void setChildrenCategories(List<ProductCategory> childrenCategories) {
		this.childrenCategories = childrenCategories;
	}

}
