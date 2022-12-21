package com.account.tax.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.tax.controller.UserController;
import com.account.tax.model.Contact;
import com.account.tax.model.Refer;
import com.account.tax.model.Role;
import com.account.tax.model.User;
import com.account.tax.model.UserDocuments;
import com.account.tax.repo.ContactRepo;
import com.account.tax.repo.ReferRepo;
import com.account.tax.repo.UserDocumentsRepo;
import com.account.tax.repo.UserRepository;
import com.sun.mail.util.MailSSLSocketFactory;

@Service
public class TaxServiceImpl implements TaxService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserDocumentsRepo userDocumentsRepo;
	
	@Autowired
	ContactRepo contactRepo;
	
	@Autowired
	ReferRepo referRepo;
	
	 @Autowired
	 private BCryptPasswordEncoder passwordEncoder;
	 
	 @Autowired
	 private JavaMailSender javaMailSender;
	 
	 public static final Logger log = LoggerFactory.getLogger(TaxServiceImpl.class);
	 
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User findUserByResetToken(String resetToken) {
		return userRepository.findByResetToken(resetToken);
	}

	@Override
	public User saveUser(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(new Role("ROLE_USER")));

		User savedUser = userRepository.save(user);
		return savedUser;
	}
	
	@Override
	public User updateUser(User user) {
		User savedUser = userRepository.save(user);
		return savedUser;
	}

	@Override
	public Optional<User> findUserById(int id) {
		Optional<User> user=userRepository.findById(id);
		return user;
	}

	@Override
	public List<UserDocuments> findByEmailId(String email) {
		
		return userDocumentsRepo.findByEmailId(email);
	}

	@Override
	public void saveDocuments(UserDocuments userD) {
		userDocumentsRepo.save(userD);
	}

	@Override
	public Optional<UserDocuments> findByDocumentId(int id) {
		
		return userDocumentsRepo.findById(id);
	}

	@Override
	public List<UserDocuments> fetchAllDocumentsByDraftFlag(String draftFlag) {
		
		return userDocumentsRepo.findAllByDraftFlagOrderByIdDesc(draftFlag);
	}

	@Override
	public void saveContact(Contact contact) {
		contactRepo.save(contact);
	}

	@Override
	public void saveReferral(Refer refer) {
		referRepo.save(refer);
		
	}

	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(),
            mapRolesToAuthorities(user.getRoles()));
    }

    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection <Role> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }

	@Override
	public List<Refer> fetchAllReferrals() {
		return referRepo.findAllByOrderByIdDesc();
	}

	@Override
	public List<Contact> fetchAllContacts() {
		return contactRepo.findAllByOrderByIdDesc();
	}
    
	@Override
	@Transactional(readOnly = true)
	public List<User> fetchUsers() {
	    return userRepository.findAllByOrderByIdDesc();
	}

	@Override
	public Optional<Contact> getContactById(int id) {
		
		return contactRepo.findById(id);
	}
	
	@Override
	public Optional<Refer> getReferById(int id) {
		
		return referRepo.findById(id);
	}

	@Override
	public List<User> fetchAdminUsers() {
		 
		 return userRepository.findAllAdminUsers();
	}
	
	@Override
	public List<User> findAllUsers() {
		
		 return userRepository.findAllUsers();
	}
	
	

	@Override
	public void sendMail(String toMail, String subject, String content)
	        throws MessagingException, UnsupportedEncodingException, GeneralSecurityException {
		log.info("Start sendEmail");
	    MimeMessage message = javaMailSender.createMimeMessage();              
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	    MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
	    socketFactory.setTrustAllHosts(true);
	    helper.setFrom("info@wintaxfiler.com", "Win Tax");
	    helper.setTo(toMail);
	     
	    helper.setSubject(subject);
	     
	    helper.setText(content, true);
	     
	    javaMailSender.send(message);
	    log.info("End sendEmail");
	}
}
