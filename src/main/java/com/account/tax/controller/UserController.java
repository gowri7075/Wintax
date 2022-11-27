package com.account.tax.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.account.tax.model.Contact;
import com.account.tax.model.Documents;
import com.account.tax.model.Login;
import com.account.tax.model.Refer;
import com.account.tax.model.Role;
import com.account.tax.model.User;
import com.account.tax.model.UserDocuments;
import com.account.tax.service.TaxService;

@Controller
public class UserController {

	@Autowired
	TaxService taxService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	User userDetails = null;

	List<User> usersDetails = null;
	List<UserDocuments> userDocuments = null;
	List<String> adminUsers = null;
	public static final Logger log = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute(name = "registerForm") User user, ModelMap model) {
		log.info("Entered Save user");
		try {
			User userRegister = taxService.findByEmail(user.getEmail());
			if (userRegister != null) {
				model.put("successMessage", "User is already registered with Email " + userRegister.getEmail());
				return "login";
			}
			User objUser = taxService.saveUser(user);
			model.put("successMessage", "Registered succesfully with user id:" + objUser.getId());

			log.info("Registered succesfully with user id:" + objUser.getId());

			String subject = "Welcome to WinTax";
			String content = "<p>ThankYou for registering with WinTax Consultant :) <p>"
					+ "<br>"
					+ " Your Registration id is : "	+ objUser.getId() 
					+ "<br> "
					+ "<br> "
					+ "Regards, "
					+ "<br>"
					+ "WinTax";
			taxService.sendMail(user.getEmail(), subject, content);

			log.info("Registration Mail Sent Succesfully");
		} catch (Exception e) {
			log.error("Exception occurred while Registering a user" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited Save user");
		return "login";
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public String forgotPassword(HttpServletRequest request, Model model,ModelMap modelMap) {
		log.info("Entered forgot Password");
		String email=request.getParameter("email");
		log.info("Entered forgot Password"+email);
		try {
			User resetUser = taxService.findByEmail(email);
			
			if (ObjectUtils.isEmpty(resetUser)) {
				modelMap.put("errorMessage",
						"Sorry !!!!! Requested User " + email + " is Not registered with WinTax ");
				return "login";
			}

			resetUser.setResetToken(UUID.randomUUID().toString());
			resetUser = taxService.updateUser(resetUser);
			String appUrl = "https://wintaxconsultant.com";
			modelMap.put("successMessage",
					"Your Password reset link is mailed to your registered Email: " + resetUser.getEmail());

			String subject = "Reset Password Link:: Win Tax";
			
			String link=appUrl + "/reset_password?token=" + resetUser.getResetToken();
			String content = "<p>Hello,</p>"
			            + "<p>You have requested to reset your password.</p>"
			            + "<p>Click the link below to change your password:</p>"
			            + "<p><a href=\"" + link + "\">Change my password</a></p>"
			            + "<br>"
			            + "<p>Ignore this email if you do remember your password, "
			            + "or you have not made the request.</p> "
			            + "<br>"
			            + "<br>"
			            + "<p> Regards, </p>"
			            + "<p> WinTax </p>";
			taxService.sendMail(resetUser.getEmail(), subject, content);

			log.info("Password reset Mail Sent Succesfully");
		} catch (Exception e) {
			log.error("Exception occurred while handling forgot password:: " + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited forgot Password");
		return "login";
	}

	// Display form to reset password
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	    User resetUser = taxService.findUserByResetToken(token);
	    model.addAttribute("token", token);
	     
	    if (resetUser == null) {
	        model.addAttribute("errorMessage", "Invalid Token / Token Expired. Please try Again");
	        return "login";
	    }
	     
	    return "resetpassword";
	}

	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	    User resetUser = taxService.findUserByResetToken(token);
	     
	    if (resetUser == null) {
	        model.addAttribute("errorMessage", "Invalid Token / Token Expired. Please try Again");
	        return "login";
	    } else {        
	    	resetUser.setPassword(passwordEncoder.encode(password));
	    	resetUser.setResetToken(null);
	    	taxService.updateUser(resetUser);
	        model.addAttribute("successMessage", "You have successfully changed your password.");
	    }
	     
	    return "login";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String displayUserHomePage(ModelMap model) {
		log.info("Entered displayUserHomePage");
		User user = loadUserDetails(model);
		String page = "";
		try {
			if (user == null) {
				return "index";
			} else {
				for (Role role : user.getRoles()) {
					if ("ROLE_ADMIN".equals(role.getName())) {
						page = "admin";
					} else {
						page = "home";
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception occurred while displaying User Home Page" + e.getMessage());
			e.printStackTrace();
		}
		log.info("Exited displayUserHomePage");
		return page;
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getIndexPage() {
		return "index";
	}

	private User loadUserDetails(ModelMap model) {
		log.info("entered loadUserDetails");
		User userRegister = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = "";
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				currentPrincipalName = authentication.getName();
			}
			userRegister = taxService.findByEmail(currentPrincipalName);
			model.put("user", userRegister);
		} catch (Exception e) {
			log.error("Exception occurred while loading user details" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited loadUserDetails");
		return userRegister;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String getHomePg(ModelMap model) {
		loadUserDetails(model);
		return "home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getlogin() {
		return "login";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String getcontact(ModelMap model) {
		log.info("Entered getcontact");
		User user = loadUserDetails(model);
		Contact contact = new Contact();
		if (user != null) {
			contact.setEmail(user.getEmail());
			contact.setName(user.getName());
			contact.setContactNumber(user.getMobileNumber());
		}
		model.put("contact", contact);
		return "contact";
	}

	@RequestMapping(value = "/refer", method = RequestMethod.GET)
	public String getRefer(ModelMap model) {
		log.info("Entered getRefer");
		User user = loadUserDetails(model);

		Refer refer = new Refer();
		if (user != null) {
			refer.setEmail(user.getEmail());
			refer.setName(user.getName());
			refer.setMobileNumber(user.getMobileNumber());
		}
		model.put("refer", refer);
		log.info("Exited getRefer");
		return "refer";
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String getDashboard(ModelMap model) {
		log.info("Entered getDashboard");

		User user = loadUserDetails(model);
		try {
			userDocuments = taxService.findByEmailId(user.getEmail());
			List<UserDocuments> docs = new ArrayList<>();
			List<UserDocuments> draftDocs = new ArrayList<>();
			for (UserDocuments userDocuments : userDocuments) {
				if ("F".equals(userDocuments.getDraftFlag())) {
					docs.add(userDocuments);
				} else {
					draftDocs.add(userDocuments);
				}
			}
			model.put("documents", docs);
			model.put("draftDocuments", draftDocs);
		} catch (Exception e) {
			log.error("Exception while displaying documents by mail id:" + e.getMessage());
			e.printStackTrace();
		}
		log.info("Exited getDashboard");

		return "dashboard";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegister() {
		return "register";
	}
	
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public String getForgotPassword() {
		return "forgotpassword";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String getLoginPage(@ModelAttribute(name = "loginForm") Login login, ModelMap model) {
		log.info("Entered getLoginPage");
		try {
			userDetails = taxService.findByEmail(login.getEmail());
			if (userDetails != null && userDetails.getPassword().equals(passwordEncoder.encode(login.getPassword()))) {
				model.put("UserName", "Welcome :" + userDetails.getName());
				model.put("user", userDetails);
				return "home";
			}
			model.put("errorMessage", "User name or password is invalid");
		} catch (Exception e) {
			log.error("Exception occurred while login in to the page" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited getLoginPage");
		return "login";

	}

	@RequestMapping(value = "/uploadDocument", method = RequestMethod.POST)
	public String uploadDocuments(@ModelAttribute(name = "documents") Documents documents, ModelMap model) {
		log.info("Entered uploadDocuments");
		UserDocuments userD = new UserDocuments();
		try {
			User user = loadUserDetails(model);
			if (documents.getDocument().getSize() > 0 && documents.getDocument().getSize() <= 3145728) {
				userD.setDocument(documents.getDocument().getBytes());
				userD.setEmailId(user.getEmail());
				userD.setDocumentName(documents.getDocument().getOriginalFilename());
				userD.setFileType(documents.getDocument().getContentType());
				userD.setDocumentType(documents.getDocumentType());
				userD.setDraftFlag("F");
				model.put("Success", "File uploaded Successfully");
				taxService.saveDocuments(userD);
				log.info("File uploaded Successfully");
			} else {
				log.info("File size cannot be more than 3MB");
				model.put("Fail", "File size cannot be more than 3MB");
			}

			if (documents.getDocument().getSize() == 0) {
				log.info("File size cannot be Empty");
				model.put("Fail", "Cannot upload empty file");
			}

		} catch (IOException e) {
			log.error("IOException occurred while trying to upload documents" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Exception occurred while trying to upload documents" + e.getMessage());
			e.printStackTrace();
		}
		try {
			getDashboard(model);
		} catch (Exception e) {
			log.error("Exception occurred while trying to ByEmailId after uploading documents" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited uploadDocuments");
		return "dashboard";

	}

	@RequestMapping(value = "/uploadDocumentsByAdmin", method = RequestMethod.POST)
	public String uploadDocumentsByAdmin(@ModelAttribute(name = "documents") Documents documents, ModelMap modelMap) {
		log.info("Entered uploadDocumentsByAdmin");
		UserDocuments userD = new UserDocuments();
		try {
			if (documents.getDocument().getSize() > 0 && documents.getDocument().getSize() <= 3145728) {
				userD.setDocument(documents.getDocument().getBytes());
				userD.setEmailId(documents.getEmailId());
				userD.setDocumentName(documents.getDocument().getOriginalFilename());
				userD.setFileType(documents.getDocument().getContentType());
				userD.setDocumentType(documents.getDocumentType());
				userD.setDraftFlag("T");
				taxService.saveDocuments(userD);
				modelMap.put("Success", "File uploaded Successfully");
			} else {
				log.info("File size cannot be more than 3MB");
				modelMap.put("Failure", "File size cannot be more than 3MB");
			}

			if (documents.getDocument().getSize() == 0) {
				log.info("File size cannot be Empty");
				modelMap.put("Failure", "Cannot upload empty file");
			}
			loadUserDetails(modelMap);
		} catch (IOException e) {
			modelMap.put("Failure", "Exception occurred while trying to upload documents");
			log.error("IOException occurred while trying to upload documents" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			modelMap.put("Failure", "Exception occurred while trying to upload documents");
			log.error("Exception occurred while trying to upload documents" + e.getMessage());
			e.printStackTrace();
		}

		try {
			List<User> users = fetchUsers();
			List<User> clients = new ArrayList<>();
			for (User user : users) {
				for (Role role : user.getRoles()) {
					if ("ROLE_USER".equals(role.getName())) {
						user.setName(user.getName() + " " + user.getLastName());
						clients.add(user);
					}
				}
			}
			List<String> currentStatus = new ArrayList<>();
			currentStatus.add("New");
			currentStatus.add("Contacted");
			currentStatus.add("Success");
			currentStatus.add("Failure");
			modelMap.put("currentStatus", currentStatus);
			modelMap.put("clients", clients);
		} catch (Exception e) {
			log.error("Exception occurred while trying to fetch contacts" + e.getMessage());
			e.printStackTrace();
		}
		log.info("Exited uploadDocumentsByAdmin");
		return "userinfo";

	}

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity downloadDocumets(@Param(value = "id") int id, ModelMap model) {
		log.info("Entered downloadDocumets");
		Optional<UserDocuments> document = null;
		try {
			document = taxService.findByDocumentId(id);
			model.put("documents", userDocuments);
			model.put("user", userDetails);
		} catch (Exception e) {
			log.error("Exception occurred while trying to Download documents" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited downloadDocumets");
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(document.get().getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + document.get().getDocumentName() + "\"")
				.body(document.get().getDocument());
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(@ModelAttribute(name = "user") User user, ModelMap model) {
		log.info("Entered updateUser");
		Optional<User> userRegister = null;
		try {
			userRegister = taxService.findUserById(user.getId());

			user.setEmail(userRegister.get().getEmail());
			user.setPassword(userRegister.get().getPassword());
			user = taxService.saveUser(user);
			userDetails = user;
			model.put("user", userDetails);
			model.put("documents", userDocuments);
		} catch (Exception e) {
			log.error("Exception occurred while trying to update User" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited updateUser");
		return "dashboard";
	}

	@RequestMapping(value = "/contactAdmin", method = RequestMethod.POST)
	public String contactAdmin(@ModelAttribute(name = "contactForm") Contact contact, ModelMap model) {
		log.info("Entered contactAdmin");
		try {
			if (contact.getStatus() == null || contact.getStatus().isEmpty()) {
				contact.setStatus("New");
			}
			taxService.saveContact(contact);
			model.put("Success", "Our representative will reach out to you shortly");
			User user = loadUserDetails(model);
			if (user != null) {
				Contact contactInfo = new Contact();
				contactInfo.setEmail(user.getEmail());
				contactInfo.setContactNumber(user.getMobileNumber());
				contactInfo.setName(user.getName());
				model.put("contact", contactInfo);
			} else {
				model.put("contact", contact);
			}

		} catch (Exception e) {
			log.error("Exception occurred while trying to save a contact" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited contactAdmin");
		return "contact";
	}

	@RequestMapping(value = "/contactAdminFromHome", method = RequestMethod.POST)
	public String contactAdminFromHome(@ModelAttribute(name = "contactForm") Contact contact, ModelMap model) {
		log.info("Entered contactAdmin");
		try {
			if (contact.getStatus() == null || contact.getStatus().isEmpty()) {
				contact.setStatus("New");
			}
			taxService.saveContact(contact);
			model.put("Success", "Our representative will reach out to you shortly");
		} catch (Exception e) {
			log.error("Exception occurred while trying to save a contact" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited contactAdminFromHome");
		return "index";
	}

	@RequestMapping(value = "/updateContactAdmin", method = RequestMethod.POST)
	public String updateContactAdmin(@ModelAttribute(name = "contactForm") Contact contact, ModelMap modelMap,
			Model model) {
		log.info("Entered contactAdmin");
		try {
			if (contact.getStatus() == null || contact.getStatus().isEmpty()) {
				contact.setStatus("New");
			}
			Optional<Contact> contactInfo = taxService.getContactById(contact.getId());
			contact.setEmail(contactInfo.get().getEmail());
			contact.setMessage(contactInfo.get().getMessage());
			contact.setName(contactInfo.get().getName());
			contact.setContactNumber(contactInfo.get().getContactNumber());
			contact.setSubject(contactInfo.get().getSubject());
			contact.setContactedDate(contactInfo.get().getContactedDate());
			taxService.saveContact(contact);
			modelMap.put("Success", "Our representative will reach out to you shortly");
			try {
				List<Contact> listContacts = taxService.fetchAllContacts();
				model.addAttribute("listContacts", listContacts);
				List<String> adminUsersList = fetchAdminUsers();
				List<String> currentStatus = new ArrayList<>();
				currentStatus.add("New");
				currentStatus.add("Contacted");
				currentStatus.add("Success");
				currentStatus.add("Failure");
				modelMap.put("currentStatus", currentStatus);
				modelMap.put("adminUsers", adminUsersList);
			} catch (Exception e) {
				log.error("Exception occurred while trying to fetch contacts" + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.error("Exception occurred while trying to save a contact" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited contactAdmin");
		return "contactrequests";
	}

	@RequestMapping(value = "/updateReferAFriendAdmin", method = RequestMethod.POST)
	public String updateReferAFriendAdmin(@ModelAttribute(name = "referForm") Refer refer, ModelMap modelMap,
			Model model) {
		log.info("Entered updateReferAFriendAdmin");
		try {
			if (refer.getStatus() == null || refer.getStatus().isEmpty()) {
				refer.setStatus("New");
			}
			Optional<Refer> referInfo = taxService.getReferById(refer.getId());
			refer.setEmail(referInfo.get().getEmail());
			refer.setMessage(referInfo.get().getMessage());
			refer.setName(referInfo.get().getName());
			refer.setMobileNumber(referInfo.get().getMobileNumber());
			refer.setReferralEmail(referInfo.get().getReferralEmail());
			refer.setReferralMobileNumber(referInfo.get().getReferralMobileNumber());
			refer.setReferralName(referInfo.get().getReferralName());
			refer.setReferredDate(referInfo.get().getReferredDate());
			taxService.saveReferral(refer);
			modelMap.put("Success", "Our representative will reach out to you shortly");
			try {
				List<Refer> listReferrals = taxService.fetchAllReferrals();
				model.addAttribute("listReferrals", listReferrals);
				List<String> adminUsersList = fetchAdminUsers();
				List<String> currentStatus = new ArrayList<>();
				currentStatus.add("New");
				currentStatus.add("Contacted");
				currentStatus.add("Success");
				currentStatus.add("Failure");
				modelMap.put("currentStatus", currentStatus);
				modelMap.put("adminUsers", adminUsersList);

			} catch (Exception e) {
				log.error("Exception occurred while trying to fetch referrals" + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.error("Exception occurred while trying to save a contact" + e.getMessage());
			e.printStackTrace();
		}
		log.info("Exited updateReferAFriendAdmin");
		return "referralrequests";
	}

	@RequestMapping(value = "/referAFriend", method = RequestMethod.POST)
	public String referAFriend(@ModelAttribute(name = "referralForm") Refer refer, ModelMap model) {

		log.info("Entered referAFriend");
		try {
			if (refer.getStatus() == null || refer.getStatus().isEmpty()) {
				refer.setStatus("New");
			}
			taxService.saveReferral(refer);
			model.put("Success", "You will receive 10$ on successfull referral");

			User user = loadUserDetails(model);

			Refer referral = new Refer();
			referral.setEmail(user.getEmail());
			referral.setName(user.getName());
			referral.setMobileNumber(user.getMobileNumber());
			model.put("refer", referral);

		} catch (Exception e) {
			log.error("Exception occurred while trying to refer a friend" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited referAFriend");
		return "refer";
	}

	@RequestMapping(value = "/displayDocuments", method = RequestMethod.GET)
	public String displayDocumentsByPagination(Model model) {
		log.info("Entered displayDocumentsByPagination");
		try {
			List<UserDocuments> listDocuments = taxService.fetchAllDocumentsByDraftFlag("F");
			model.addAttribute("listDocuments", listDocuments);
		} catch (Exception e) {
			log.error("Exception occurred while trying to fetch documents" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited displayDocumentsByPagination");
		return "admin";
	}

	@RequestMapping(value = "/contactRequests", method = RequestMethod.GET)
	public String contactRequestsByPagination(Model model, ModelMap modelMap) {
		log.info("Entered contactRequestsByPagination");
		try {
			List<Contact> listContacts = taxService.fetchAllContacts();
			model.addAttribute("listContacts", listContacts);
			List<String> adminUsersList = fetchAdminUsers();
			List<String> currentStatus = new ArrayList<>();
			currentStatus.add("New");
			currentStatus.add("Contacted");
			currentStatus.add("Success");
			currentStatus.add("Failure");
			modelMap.put("currentStatus", currentStatus);
			modelMap.put("adminUsers", adminUsersList);
		} catch (Exception e) {
			log.error("Exception occurred while trying to fetch contacts" + e.getMessage());
			e.printStackTrace();
		}
		log.info("Exited contactRequestsByPagination");
		return "contactrequests";
	}

	private List<String> fetchAdminUsers() {
		log.info("started fetching admin users");
		adminUsers = new ArrayList<>();
		List<User> users = taxService.fetchAdminUsers();
		log.info(":::::::started fetching admin users{}", ObjectUtils.isEmpty(users) ? 0 : users.size());
		for (User user : users) {
			for (Role role : user.getRoles()) {
				if ("ROLE_ADMIN".equals(role.getName())) {
					adminUsers.add(user.getName() + " " + user.getLastName());
				}
			}
		}
		log.info("Exited fetching admin users");
		return adminUsers;
	}

	@RequestMapping(value = "/usersInfo", method = RequestMethod.GET)
	public String userInfoByPagination(Model model, ModelMap modelMap) {
		log.info("Entered userInfoByPagination");
		try {
			List<User> users = fetchUsers();
			List<User> clients = new ArrayList<>();
			for (User user : users) {
				for (Role role : user.getRoles()) {
					if ("ROLE_USER".equals(role.getName())) {
						user.setName(user.getName() + " " + user.getLastName());
						clients.add(user);
					}
				}
			}
			List<String> currentStatus = new ArrayList<>();
			currentStatus.add("New");
			currentStatus.add("Contacted");
			currentStatus.add("Success");
			currentStatus.add("Failure");
			modelMap.put("currentStatus", currentStatus);
			modelMap.put("clients", clients);
		} catch (Exception e) {
			log.error("Exception occurred while trying to fetch contacts" + e.getMessage());
			e.printStackTrace();
		}
		log.info("Exited userInfoByPagination");
		return "userinfo";
	}

	@RequestMapping(value = "/referralRequests", method = RequestMethod.GET)
	public String referralRequestsByPagination(Model model, ModelMap modelMap) {
		log.info("Entered referralRequestsByPagination");
		try {
			List<Refer> listReferrals = taxService.fetchAllReferrals();
			model.addAttribute("listReferrals", listReferrals);
			List<String> adminUsersList = fetchAdminUsers();
			List<String> currentStatus = new ArrayList<>();
			currentStatus.add("New");
			currentStatus.add("Contacted");
			currentStatus.add("Success");
			currentStatus.add("Failure");
			modelMap.put("currentStatus", currentStatus);
			modelMap.put("adminUsers", adminUsersList);

		} catch (Exception e) {
			log.error("Exception occurred while trying to fetch referrals" + e.getMessage());
			e.printStackTrace();
		}

		log.info("Exited referralRequestsByPagination");
		return "referralrequests";
	}

	@GetMapping("/admin")
	public String viewAdminPage(Model model, ModelMap modelMap) {
		log.info("Entered viewAdminPage");
		return userInfoByPagination(model, modelMap);
	}

	public List<User> fetchUsers() {
		log.info("Entered fetchUsers {} ", ObjectUtils.isEmpty(usersDetails) ? 0 : usersDetails.size());
		usersDetails = taxService.findAllUsers();
		return usersDetails;
	}
}
