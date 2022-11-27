package com.account.tax.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.tax.model.UserDocuments;

public interface UserDocumentsRepo extends JpaRepository<UserDocuments, Integer>{

	List<UserDocuments> findByEmailId(String emailId);

	List<UserDocuments> findAllByOrderByIdDesc();

	List<UserDocuments> findAllByDraftFlagOrderByIdDesc(String draftFlag);

}
