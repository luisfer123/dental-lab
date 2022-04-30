package com.dental.lab.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.CreateUserFormPayload;
import com.dental.lab.services.UserService;

@Controller
@RequestMapping(path = "/admin/users")
public class AdminUsersController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(path = "/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView goUsersList(ModelMap model) {
		
		List<User> users = userService.findAll();
		
		model.addAttribute("users", users);
		
		return new ModelAndView("users/admin-users", model);
	}
	
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

}
