package com.account.tax.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.account.tax.model.Contact;
import com.account.tax.model.Refer;
import com.account.tax.model.User;
import com.account.tax.model.UserDocuments;

public interface TaxService extends UserDetailsService{

	User findByEmail(String email);

	User saveUser(User user);

	Optional<User> findUserById(int id);

	List<UserDocuments> findByEmailId(String email);

	void saveDocuments(UserDocuments userD);

	Optional<UserDocuments> findByDocumentId(int id);

	List<UserDocuments> fetchAllDocumentsByDraftFlag(String draftFlag);

	void saveContact(Contact contact);

	void saveReferral(Refer refer);

	List<Refer> fetchAllReferrals();

	List<Contact> fetchAllContacts();

	List<User> fetchUsers();

	Optional<Contact> getContactById(int id);

	Optional<Refer> getReferById(int id);

	List<User> fetchAdminUsers();

	List<User> findAllUsers();


	User findUserByResetToken(String resetToken);

	User updateUser(User resetUser);

	void sendMail(String toMail, String subject, String content)
			throws MessagingException, UnsupportedEncodingException, GeneralSecurityException;

	
}
