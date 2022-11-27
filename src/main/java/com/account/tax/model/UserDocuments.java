package com.account.tax.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class UserDocuments {

	@Id
    @GeneratedValue
	private int id;
	
	@Column(name = "email_Id", columnDefinition="VARCHAR(128)")
	private String emailId;
	
	@Lob
    @Column(name = "document", columnDefinition="LONGBLOB")
    private byte[] document;

	@Column(name = "document_Type", columnDefinition="VARCHAR(128)")
	private String documentType;
	
	@Column(name = "document_Name", columnDefinition="VARCHAR(128)")
	private String documentName;
	
	@Column(name = "file_Type", columnDefinition="VARCHAR(128)")
	private String fileType;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "draft_Flag", columnDefinition="VARCHAR(128)")
	private String draftFlag;
	
	/**
	 * @return the draftFlag
	 */
	public String getDraftFlag() {
		return draftFlag;
	}

	/**
	 * @param draftFlag the draftFlag to set
	 */
	public void setDraftFlag(String draftFlag) {
		this.draftFlag = draftFlag;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the document
	 */
	public byte[] getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(byte[] document) {
		this.document = document;
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

	/**
	 * @return the documentName
	 */
	public String getDocumentName() {
		return documentName;
	}

	/**
	 * @param documentName the documentName to set
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
}
