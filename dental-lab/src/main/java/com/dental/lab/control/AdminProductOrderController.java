package com.dental.lab.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductOrder;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EProductItemStatus;
import com.dental.lab.model.payloads.OrdersListViewPageDetails;
import com.dental.lab.model.payloads.PreOrderCurrentStatePayload;
import com.dental.lab.model.payloads.PreOrderProductInfo;
import com.dental.lab.model.payloads.ProductViewPayload;
import com.dental.lab.model.payloads.UserSearchPayload;
import com.dental.lab.services.OrderService;
import com.dental.lab.services.ProductCategoryService;
import com.dental.lab.services.ProductService;
import com.dental.lab.services.UserService;

@Controller
@RequestMapping(path = "/admin/orders")
@SessionAttributes(names = {"preOrderProducState", "paginationDetails"})
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
	
	@ModelAttribute("paginationDetails")
	public OrdersListViewPageDetails paginationDetails() {
		// Set default values when it is created
		return new OrdersListViewPageDetails(9, "creationDate");
	}
	
	@GetMapping(path = "")
	public ModelAndView goUserProductOrders(
			@ModelAttribute("preOrderProductState") PreOrderCurrentStatePayload orderState,
			@ModelAttribute("paginationDetails") OrdersListViewPageDetails paginationDetails,
			@RequestParam("pageNum") Optional<Integer> optPageNum,
			@RequestParam("user_id") Optional<Long> optUserId,
			ModelMap model) {
		
		User user = null;
		
		try {
			Long userId = optUserId.get();
			user = userService.findById(userId);
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/orders/create");
		} catch(UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/orders/create");
		}
		
		Page<ProductOrder> ordersPage;
		int pageNum = optPageNum.orElse(0);
		
		if(paginationDetails.getProductItemStatusFilter() != null) {
			ordersPage = orderService
					.findByUserIdWithProductItemsWithGivenStatus(
							user.getId(), 
							paginationDetails.getProductItemStatusFilter(), 
							pageNum, 
							paginationDetails.getPageSize(),
							paginationDetails.getSortBy());
		} else {
			 ordersPage = 
						orderService.findByUserIdWithProductItems(
								user.getId(),
								pageNum,
								paginationDetails.getPageSize(),
								paginationDetails.getSortBy());
		}
		
		int totalPages = ordersPage.getTotalPages();
		List<Integer> pageNumbers = new ArrayList<>();
		if(totalPages > 0) {
			pageNumbers = IntStream.range(0, totalPages)
					.boxed()
					.collect(Collectors.toList());
		}
				
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", ordersPage.getNumber());
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("orders", ordersPage.toList());
		model.addAttribute("user", user);
		model.addAttribute("productItemStatusList", EProductItemStatus.values());
		
		return new ModelAndView("/orders/admin-orders-by-user", model);
	} 
	
	@GetMapping(path = "/create")
	public ModelAndView goAdminCreateOrder(
			@RequestParam(name = "category_id", required = false) Optional<Long> optCategoryId,
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			@Valid @ModelAttribute("userSearchPayload") UserSearchPayload userSearchPayload,
			BindingResult result,
			ModelMap model) {
		
		// ======== Search user logic ==================================
		
		if((orderState.getSelectedUserUsername() == null || orderState.getSelectedUserUsername().length() == 0) 
				&& userSearchPayload != null && userSearchPayload.getSearchBy() != null
				&& userSearchPayload.getSearchKeyword() != null) {
		
			if(result.hasErrors()) {
				// return new ModelAndView("users/admin-user-search", result.getModel());
			}
						
			List<User> usersFound = new ArrayList<>();
			List<User> similarUsers = new ArrayList<>();
					
			switch(userSearchPayload.getSearchBy()) {
			case "fullLastName":
				usersFound = userService.findByFullLastName(userSearchPayload.getSearchKeyword());
				model.addAttribute("usersFound", usersFound);
				
				if(usersFound == null || usersFound.isEmpty()) {
					similarUsers = userService.findSimilarUsersByFullLastName(
							userSearchPayload.getSearchKeyword(), 3);
					if(similarUsers != null && !similarUsers.isEmpty()) {
						model.addAttribute("similarUsers", similarUsers);
					} else {
						model.addAttribute("userNotFound", true);
					}
				}
				break;
			case "username":

				try {
					usersFound = Arrays.asList(
							userService.findByUsername(userSearchPayload.getSearchKeyword()));
					model.addAttribute("usersFound", usersFound);
				} catch(UserNotFoundException e) {
					similarUsers = userService.findSimilarUsersByUsername(
							userSearchPayload.getSearchKeyword(), 3);
					if(similarUsers != null && !similarUsers.isEmpty()) {
						model.addAttribute("similarUsers", similarUsers);
					} else {
						model.addAttribute("userNotFound", true);
					}
				}
				break;
			case "email":
				try {
					usersFound = Arrays.asList(
							userService.findByEmail(userSearchPayload.getSearchKeyword()));
					model.addAttribute("usersFound", usersFound);
				} catch(UserNotFoundException e) {
					similarUsers = userService.findSimilarUsersByEmail(
							userSearchPayload.getSearchKeyword(), 5);
					if(similarUsers != null && !similarUsers.isEmpty()) {
						model.addAttribute("similarUsers", similarUsers);
					} else {
						model.addAttribute("userNotFound", true);
					}
				}
				break;
			}
		}
		
		// ======== End of Search user logic ==================================
		
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
		
		model.addAttribute("userSearchPayload", new UserSearchPayload());
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
	
	@GetMapping(path = "/select_user")
	public ModelAndView selectUserPreOrder(
			@RequestParam("user_id") Optional<Long> optUserId,
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			ModelMap model) {
		
		if(orderService.hasCredentialsCreateOrderSelectUser()) {
			try {
				Long userId = optUserId.get();
				orderState.setSelectedUserUsername(
						userService.findById(userId).getUsername());
			} catch(NoSuchElementException e) {
				return new ModelAndView("redirect:/admin/orders/create");
			} catch(UserNotFoundException e) {
				return new ModelAndView("redirect:/admin/orders/create");
			}
		}
		
		return new ModelAndView("redirect:/admin/orders/create");
	}
	
	@GetMapping(path = "/deselect_user")
	public ModelAndView deselectUserPreOrder(
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			ModelMap model) {
		
		orderState.setSelectedUserUsername(null);
		
		return new ModelAndView("redirect:/admin/orders/create");
	}
	
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public ModelAndView createNewOrder(
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			ModelMap model) {
		
		User user = null;
		
		try {
			orderService.createOrder(orderState);
			orderState.clean();
			
			user = userService.findByUsername(orderState.getSelectedUserUsername());
		} catch(Exception e) {
			return new ModelAndView("redirect:/admin/orders/create");
		}
		
		return new ModelAndView("redirect:/admin/orders?user_id=" + user.getId());
	}
	
	@PostMapping(path = "/item-status")
	public ModelAndView setProductItemStatusFilter(
			@ModelAttribute("paginationDetails") OrdersListViewPageDetails paginationDetails,
			@RequestParam("updatedProductItemStatusFilter") String upisf,
			@RequestParam("userId") Long userId,
			ModelMap model) {
		
		switch(upisf) {
		case "ALL":
			paginationDetails.setProductItemStatusFilter(null);
			break;
		case "RECEIVED":
			paginationDetails.setProductItemStatusFilter(EProductItemStatus.RECEIVED);
			break;
		case "ACCEPTED":
			paginationDetails.setProductItemStatusFilter(EProductItemStatus.ACCEPTED);
			break;
		case "READY":
			paginationDetails.setProductItemStatusFilter(EProductItemStatus.READY);
			break;
		case "DELIVERED":
			paginationDetails.setProductItemStatusFilter(EProductItemStatus.DELIVERED);
			break;
		}
		
		return new ModelAndView("redirect:/admin/orders?user_id=" + userId);
	}

}
