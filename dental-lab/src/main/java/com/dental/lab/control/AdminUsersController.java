package com.dental.lab.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.CreateUserFormPayload;
import com.dental.lab.model.payloads.EditUserPayload;
import com.dental.lab.model.payloads.UserSearchPayload;
import com.dental.lab.services.AddressService;
import com.dental.lab.services.PhoneService;
import com.dental.lab.services.UserService;

@Controller
@RequestMapping(path = "/admin/users")
public class AdminUsersController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private PhoneService phoneService;
	
	@RequestMapping(path = "/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView goUsersList(ModelMap model) {
		
		List<User> users = userService.findAll();
		
		model.addAttribute("users", users);
		
		return new ModelAndView("users/admin-users", model);
	}
	
	/**
	 * 
	 * @param model
	 * @param optFromUserIdInvalid will be present, with value true, only if the request was
	 * redirected from a request handler method where a valid User id was required but
	 * not valid user id was present.
	 * @param optPageNum page number we are requesting
	 * @param optPageSize the size of the page we want to receive
	 * @param optSortBy name of the parameter we want to user to sort the page's
	 * elements
	 * @return
	 */
	@RequestMapping(path = "")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView goUsersListPaginated(
			ModelMap model,
			@RequestParam("page_num") Optional<Integer> optPageNum,
			@RequestParam("page_size") Optional<Integer> optPageSize,
			@RequestParam("sort_by") Optional<String> optSortBy) {
		
		int pageNum = optPageNum.orElse(0);
		int pageSize = optPageSize.orElse(3);
		String sortBy = optSortBy.orElse("lastName");
		
		Page<User> usersPage = 
				userService.findAll(pageNum, pageSize, sortBy);
		
		int totalPages = usersPage.getTotalPages();
		// List form 0 to the number of pages -1
		List<Integer> pageNumbers;
		
		if(totalPages > 0) {
			pageNumbers = 
					IntStream.range(0, totalPages)
					.boxed()
					.collect(Collectors.toList());
		} else {
			pageNumbers = new ArrayList<Integer>();
		}
		
		model.addAttribute("users", usersPage.getContent());
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("totalPages", totalPages);
		
		return new ModelAndView("users/admin-users", model);
	}
	
	@RequestMapping(path = "/add")
	public ModelAndView goAddUserForm(ModelMap model) {
				
		model.addAttribute("newUser", new CreateUserFormPayload());
		model.addAttribute("roles", EAuthority.values());
		
		return new ModelAndView("users/add-user");
	}
	
	@PostMapping(path = "/add")
	public ModelAndView addUser(
			@ModelAttribute("newUser") CreateUserFormPayload newUserPayload, ModelMap model) {
		
		userService.saveNewUser(newUserPayload);
		
		return new ModelAndView("redirect:/admin/users/add");
	}
	
	@GetMapping(path = "/edit")
	public ModelAndView goEditUser(
			@RequestParam("user_id") Optional<Long> optUserId,
			RedirectAttributes redirectAttrs ,ModelMap model) {
		
		Long userId;
		User user = new User();
		
		//Check whether the id parameter is included in the request
		try {
			userId = optUserId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/users/search");
		}
		
		// Try to find an User in the database with the passed id parameter
		try {
			user = userService.findById(userId);
		} catch(UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/users/search");
		}
				
		model.addAttribute("userEdited", EditUserPayload.buildFromUser(user));
		model.addAttribute("addresses", addressService.getByUser(user));
		model.addAttribute("phones", phoneService.findByUser(user));
		model.addAttribute("userId", userId);
		
		return new ModelAndView("users/admin-edit-user");
		
	}
	
	@PostMapping(path = "/edit")
	public ModelAndView editUser(
			@Valid @ModelAttribute("userEdited") EditUserPayload userEdited,
			BindingResult result,
			@RequestParam("user_id") Long userId,
			ModelMap model) {
		
		userService.saveEditedUser(userEdited, userId);
		
		if(result.hasErrors())
			result.getAllErrors().forEach(err -> {
				System.out.println(err.getDefaultMessage());
			});
		
		return new ModelAndView("users/admin-edit-user");
	}
	
	@PostMapping(path = "/search")
	public ModelAndView searchUser(
			@Valid @ModelAttribute("userSearchPayload") UserSearchPayload userSearchPayload,
			BindingResult result,
			ModelMap model) {
		
		if(result.hasErrors()) {
			return new ModelAndView("users/admin-user-search", result.getModel());
		}
		
		List<User> usersFound = new ArrayList<>();
				
		switch(userSearchPayload.getSearchBy()) {
		case "fullLastName":
			try {
				usersFound = userService.findByFullLastName(
						userSearchPayload.getSearchKeyword());
				
				// Just to execute the catch block in case no users with such last name were found
				if(usersFound == null || usersFound.isEmpty())
					throw new RuntimeException();
			} catch(Exception e) {
				model.addAttribute("similarUsers",
						userService.findSimilarUsersByFullLastName(userSearchPayload.getSearchKeyword(), 3));
				return new ModelAndView("users/admin-user-search", model);
			}
			break;
		case "username":
			try {
				usersFound = Arrays.asList(
						userService.findByUsername(userSearchPayload.getSearchKeyword()));
			} catch(UserNotFoundException e) {
				model.addAttribute("similarUsers",
						userService.findSimilarUsersByUsername(
								userSearchPayload.getSearchKeyword(), 3));
				return new ModelAndView("users/admin-user-search", model);
			}
			break;
		case "email":
			try {
				usersFound = Arrays.asList(
						userService.findByEmail(userSearchPayload.getSearchKeyword()));
			} catch(UserNotFoundException e) {
				model.addAttribute("similarUsers",
						userService.findSimilarUsersByEmail(
								userSearchPayload.getSearchKeyword(), 5));
				return new ModelAndView("users/admin-user-search", model);
			}
			break;
		}
		
		model.addAttribute("usersFound", usersFound);
		return new ModelAndView("users/admin-user-search", model);
	}
	
	@GetMapping(path = "/search")
	public ModelAndView goSearchUser(ModelMap model) {
		model.addAttribute("userSearchPayload", new UserSearchPayload());
		return new ModelAndView("users/admin-user-search", model);
	}

}
