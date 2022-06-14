package com.dental.lab.control;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dental.lab.exceptions.CustomValidationException;
import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.UpdatePasswordPayload;
import com.dental.lab.model.payloads.UpdateUserFullNamePayload;
import com.dental.lab.services.AddressService;
import com.dental.lab.services.PhoneService;
import com.dental.lab.services.UpdateUserService;
import com.dental.lab.services.UserService;
import com.dental.lab.services.UserValidationService;

@Controller
@Validated
@RequestMapping(path = "/admin/users/edit")
public class AdminEditUserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UpdateUserService updateUserService;
	
	@Autowired
	private UserValidationService userValidationService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private PhoneService phoneService;
	
	@GetMapping(path = "")
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
			user = userService.findByIdWithAuthorities(userId);
			
		} catch(UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/users/search");
		}
				
		model.addAttribute("user", user);
		model.addAttribute("addresses", addressService.getByUser(user));
		model.addAttribute("phones", phoneService.findByUser(user));
		model.addAttribute("userId", userId);
		model.addAttribute("roles", EAuthority.values());
		model.addAttribute("currentRoles", getUserCurrentAuthoritesAsEnums(user));
		
		return new ModelAndView("users/admin-edit-user");
		
	}
	
	@PostMapping(path = "/username")
	public ModelAndView updateUsername(
			@RequestParam("username") String username,
			@RequestParam("user_id") Optional<Long> OptUserId,
			ModelMap model) {
		
		Long userId = null;
		User user = null;
		
		try {
			userId = OptUserId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		try {
			user = userService.findByIdWithAuthorities(userId);
		} catch(UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		try {
			userValidationService.validateUsername(username); 
		} catch(CustomValidationException e) {
			model.addAttribute("invalidUsername", e.getErrorMessages());
			model.addAttribute("userId", userId);
			model.addAttribute("user", user);
			model.addAttribute("roles", EAuthority.values());
			model.addAttribute("currentRoles", getUserCurrentAuthoritesAsEnums(user));
			return new ModelAndView("users/admin-edit-user", model);
		}

		updateUserService.updateUsername(userId, username);
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
	}
	
	@PostMapping(path = "/email")
	public ModelAndView updateEmail(
			@RequestParam("user_id") Optional<Long> optUserId,
			@RequestParam("email") String email,
			ModelMap model) {
		
		Long userId = null;
		User user = null;
		
		try {
			userId = optUserId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		try {
			user = userService.findByIdWithAuthorities(userId);
		} catch(UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		try {
			userValidationService.validateEmail(email); 
		} catch(CustomValidationException e) {
			model.addAttribute("invalidEmail", e.getErrorMessages());
			model.addAttribute("userId", userId);
			model.addAttribute("user", user);
			model.addAttribute("roles", EAuthority.values());
			model.addAttribute("currentRoles", getUserCurrentAuthoritesAsEnums(user));
			return new ModelAndView("users/admin-edit-user", model);
		}

		try {
			updateUserService.updateEmail(userId, email);
		} catch(UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
	}
	
	@PostMapping(path = "/name")
	public ModelAndView updateName(
			@RequestParam("user_id") Optional<Long> optUserId,
			@ModelAttribute UpdateUserFullNamePayload updatedName,
			ModelMap model) {
				
		Long userId = null;
		
		try {
			userId = optUserId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		try {
			updateUserService.updateFullName(userId, updatedName);
		} catch (UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
	}
	
	@PostMapping(path = "/roles")
	public ModelAndView updateRoles(
			@RequestParam(value = "selectedRoles", required = false) List<String> selectedRoles,
			@RequestParam("user_id") Optional<Long> optUserId,
			ModelMap model) {
		
		Long userId = null;
		
		try {
			userId = optUserId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		try {
			updateUserService.updateAuthorities(userId, selectedRoles);
		} catch(Exception e) {
			System.out.println(e);
			return new ModelAndView("redirect:/admin/users");
		}
		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
	}
	
	@PostMapping(path = "/password")
	public ModelAndView updatePassword(
			@RequestParam("user_id") Optional<Long> optUserId,
			@ModelAttribute UpdatePasswordPayload updatePassword,
			ModelMap model) {
		
		Long userId = null;
		User user = null;
		
		try {
			userId = optUserId.get();
		} catch(NoSuchElementException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		try {
			user = userService.findByIdWithAuthorities(userId);
		} catch(UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/users");
		}
		
		try {
			userValidationService.validateUpdatePassword(updatePassword);
		} catch(CustomValidationException e) {
			model.addAttribute("invalidPassword", e.getErrorMessages());
			model.addAttribute("userId", userId);
			model.addAttribute("user", user);
			model.addAttribute("roles", EAuthority.values());
			model.addAttribute("currentRoles", getUserCurrentAuthoritesAsEnums(user));
			return new ModelAndView("users/admin-edit-user", model);
		}
		
		try {
			updateUserService.updatePassword(userId, updatePassword.getNewPassword());
		}  catch(UserNotFoundException e) {
			return new ModelAndView("redirect:/admin/users");
		}

		
		return new ModelAndView("redirect:/admin/users/edit?user_id=" + userId);
	}
	
	private Set<EAuthority> getUserCurrentAuthoritesAsEnums(User user) {
		return user.getAuthorities()
				.stream()
				.map(auth -> auth.getAuthority())
				.collect(Collectors.toSet());
	}

}
