package com.dental.lab.control;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductOrder;
import com.dental.lab.model.enums.EProductItemStatus;
import com.dental.lab.model.payloads.OrdersListViewPageDetails;
import com.dental.lab.model.payloads.PreOrderCurrentStatePayload;
import com.dental.lab.model.payloads.PreOrderProductInfo;
import com.dental.lab.model.payloads.ProductViewPayload;
import com.dental.lab.security.CustomUserDetails;
import com.dental.lab.services.OrderService;
import com.dental.lab.services.ProductCategoryService;
import com.dental.lab.services.ProductService;

@Controller
@RequestMapping(path = "/orders")
@SessionAttributes(names = {
		"preOrderProducState", "paginationDetails"})
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
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
	public ModelAndView goOrders(
			@ModelAttribute("paginationDetails") OrdersListViewPageDetails paginationDetails,
			@RequestParam("pageNum") Optional<Integer> optPageNum,
			ModelMap model) {
				
		CustomUserDetails principal = 
				(CustomUserDetails) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
		
		Page<ProductOrder> ordersPage;
		int pageNum = optPageNum.orElse(0);
		
		if(paginationDetails.getProductItemStatusFilter() != null) {
			ordersPage = orderService
					.findByUserIdWithProductItemsWithGivenStatus(
							principal.getId(), 
							paginationDetails.getProductItemStatusFilter(), 
							pageNum, 
							paginationDetails.getPageSize(),
							paginationDetails.getSortBy());
		} else {
			 ordersPage = 
						orderService.findByUserIdWithProductItems(
								principal.getId(),
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
		
		return new ModelAndView("/orders/orders", model);
	}
	
	@GetMapping(path = "/create")
	public ModelAndView goCreateOrder(
			@RequestParam(name = "category_id", required = false) Optional<Long> optCategoryId,
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			ModelMap model) {
		
		CustomUserDetails principal = 
				(CustomUserDetails) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
		
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
		
		List<ProductViewPayload> productsById = 
				productService.findByCategoryId(category.getId())
				.stream()
				.map(product -> ProductViewPayload.build(product))
				.sorted(Comparator.comparing(pv -> pv.getName()))
				.collect(Collectors.toList());
		
		model.addAttribute("category", category);
		model.addAttribute("subCategories", subCategories);
		model.addAttribute("products", productsById);
		model.addAttribute("categoryPath", categoryService.findCategoryPath(category.getId()));
		model.addAttribute("userId", principal.getId());
		
		return new ModelAndView("/orders/create-order", model);
	}
	
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public ModelAndView createNewOrder(
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			ModelMap model) {
		
		try {
			orderService.createOrder(orderState);
			orderState.clean();
		} catch(Exception e) {
			return new ModelAndView("redirect:/orders/create");
		}
		
		return new ModelAndView("redirect:/orders");
	}
	
	@RequestMapping(path = "/create/select_product", method = RequestMethod.POST)
	public ModelAndView addSelectedProduct(
			@ModelAttribute("preOrderProducState") PreOrderCurrentStatePayload orderState,
			@ModelAttribute("newProductInfo") PreOrderProductInfo productInfo,
			ModelMap model) {

		if(productInfo.getProductId() != null &&
				productService.existsById(productInfo.getProductId())) {
			orderState.addProductInfo(productInfo);
		}
		
		return new ModelAndView("redirect:/orders/create");
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
		
		return new ModelAndView("redirect:/orders/create");
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
		
		return new ModelAndView("redirect:/orders/create");
	}
	
	@PostMapping(path = "/pagination")
	public ModelAndView setPaginationDetails(
			@ModelAttribute("paginationDetails") OrdersListViewPageDetails paginationDetails,
			@ModelAttribute("updatedPaginationDetails") OrdersListViewPageDetails upd,
			ModelMap model) {
		
		paginationDetails.setPageSize(upd.getPageSize());
		paginationDetails.setSortBy(upd.getSortBy());
		
		return new ModelAndView("redirect:/orders");
	}
	
	@PostMapping(path = "/item-status")
	public ModelAndView setProductItemStatusFilter(
			@ModelAttribute("paginationDetails") OrdersListViewPageDetails paginationDetails,
			@RequestParam("updatedProductItemStatusFilter") String upisf,
			ModelMap model) {
		
		switch(upisf) {
		case "ALL":
			System.out.println("ALL executed");
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
		
		return new ModelAndView("redirect:/orders");
	}

}
