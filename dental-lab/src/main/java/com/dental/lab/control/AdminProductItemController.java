package com.dental.lab.control;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.exceptions.ProductItemNotFoundException;
import com.dental.lab.model.enums.EProductItemStatus;
import com.dental.lab.services.ProductItemService;

@Controller
@RequestMapping(path = "/admin/product-item")
public class AdminProductItemController {

	@Autowired
	private ProductItemService productItemService;

	@PostMapping(path = "/update-status")
	public ModelAndView updateProductItemStatus(@RequestParam("productItemId") Optional<Long> optProductItemId,
			@RequestParam("userId") Optional<Long> optUserId,
			@RequestParam("newProductItemStatus") EProductItemStatus newProductItemStatus, ModelMap model) {

		Long userId = null;
		
		try {
			userId = optUserId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/orders");
		}
		
		try {
			productItemService.updateProductItemStatus(
					optProductItemId.get(), newProductItemStatus);

		} catch (ProductItemNotFoundException e) {
			return new ModelAndView("redirect:/admin/orders?user_id=" + userId);
		} catch (IllegalArgumentException e) {
			return new ModelAndView("redirect:/admin/orders?user_id=" + userId);
		} catch (NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/orders?user_id=" + userId);
		}

		return new ModelAndView("redirect:/admin/orders?user_id=" + userId);
	}

}
