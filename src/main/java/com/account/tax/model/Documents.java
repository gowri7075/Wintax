package com.account.tax.model;

import org.springframework.web.multipart.MultipartFile;

public class Documents {

	private MultipartFile document;

	private String emailId;
	
	private String documentType;
	
	/**
	 * @return the document
	 */
	public MultipartFile getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(MultipartFile document) {
		this.document = document;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType the documentType to set
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	
}
