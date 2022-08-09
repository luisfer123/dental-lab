package com.dental.lab.control;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.payloads.PreOrderCurrentStatePayload;
import com.dental.lab.model.payloads.PreOrderProductInfo;
import com.dental.lab.model.payloads.ProductViewPayload;
import com.dental.lab.services.OrderService;
import com.dental.lab.services.ProductCategoryService;
import com.dental.lab.services.ProductService;
import com.dental.lab.services.UserService;

@Controller
@RequestMapping(path = "/admin/orders")
@SessionAttributes(names = {"preOrderProducState"})
public class AdminProductOrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService categoryService;
	
	@ModelAttribute("preOrderProducState")
	public PreOrderCurrentStatePayload preOrderProducState() {
		return new PreOrderCurrentStatePayload();
	}
	
	@GetMapping(path = "/create")
	public ModelAndView goAdminCreateOrder(
			@RequestParam(name = "category_id", required = false) Optional<Long> optCategoryId,
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			ModelMap model) {
		
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
		
		List<ProductViewPayload> productsByCategoryId = 
				productService.findByCategoryId(category.getId())
				.stream()
				.map(product -> ProductViewPayload.build(product))
				.sorted(Comparator.comparing(pv -> pv.getName()))
				.collect(Collectors.toList());
		
		User selectedUser = null;
		try {
			selectedUser = 
					userService.findByUsername(orderState.getSelectedUserUsername());
		} catch(UserNotFoundException e) {
			model.addAttribute("invalidSelectedUser", true);
		}
		
		model.addAttribute("selectedUser", selectedUser);
		model.addAttribute("category", category);
		model.addAttribute("subCategories", subCategories);
		model.addAttribute("products", productsByCategoryId);
		model.addAttribute("categoryPath", categoryService.findCategoryPath(category.getId()));
		
		return new ModelAndView("/orders/admin-create-order", model);
	}
	
	@RequestMapping(path = "/create/select_product", method = RequestMethod.POST)
	public ModelAndView addPreOrderSelectedProduct(
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			@ModelAttribute("newProductInfo") PreOrderProductInfo productInfo,
			ModelMap model) {

		if(productInfo.getProductId() != null &&
				productService.existsById(productInfo.getProductId())) {
			orderState.addProductInfo(productInfo);
		}
		
		return new ModelAndView("redirect:/admin/orders/create");
	}
	
	@RequestMapping(path = "/create/delete_product")
	public ModelAndView removePreOrderProduct(
			@RequestParam("product_temp_id") Optional<Integer> optTempId,
			@RequestParam("product_id") Optional<Long> optProductId,
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			ModelMap model) {

		if(optProductId.isPresent() && optTempId.isPresent()) {
			Long productId = optProductId.get();
			Integer productTempId = optTempId.get();
			orderState.removeProductInfo(productTempId, productId);
		}
		
		return new ModelAndView("redirect:/admin/orders/create");
	}
	
	@RequestMapping(path = "/create/edit_selected_product", method = RequestMethod.POST)
	public ModelAndView EditPreOrderProduct(
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			@ModelAttribute("productInfo") PreOrderProductInfo productInfo,
			ModelMap model) {
		
		if(orderState.removeProductInfo(
				productInfo.getTempId(), productInfo.getProductId())) {
			orderState.addProductInfo(productInfo);
		}
		
		return new ModelAndView("redirect:/admin/orders/create");
	}
	
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public ModelAndView createNewOrder(
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			ModelMap model) {
		
		return new ModelAndView("redirect:/admin/orders/create");
		
	}

}
