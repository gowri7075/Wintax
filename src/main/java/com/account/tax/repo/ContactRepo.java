package com.account.tax.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.tax.model.Contact;

public interface ContactRepo extends JpaRepository<Contact, Integer>{

	List<Contact> findAllByOrderByIdDesc();

}
