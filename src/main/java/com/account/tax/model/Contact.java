package com.account.tax.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Contact {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(name = "name", columnDefinition="VARCHAR(128)")
	private String name;
	
	@Column(name = "email", columnDefinition="VARCHAR(128)")
	private String email;
	
	@Column(name = "contactNumber", columnDefinition="VARCHAR(128)")
	private String contactNumber;
	
	@Column(name = "subject", columnDefinition="VARCHAR(128)")
	private String subject;
	
	@Column(name = "message", columnDefinition="VARCHAR(128)")
	private String message;
	
	@Column(name = "responded_by", columnDefinition="VARCHAR(128)")
	private String respondedBy;
	
	@Column(name = "status", columnDefinition="VARCHAR(128)")
	private String status;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "contacted_date")
	private Date contactedDate;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the contactedDate
	 */
	public Date getContactedDate() {
		return contactedDate;
	}
	/**
	 * @param contactedDate the contactedDate to set
	 */
	public void setContactedDate(Date contactedDate) {
		this.contactedDate = contactedDate;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the respondedBy
	 */
	public String getRespondedBy() {
		return respondedBy;
	}
	/**
	 * @param respondedBy the respondedBy to set
	 */
	public void setRespondedBy(String respondedBy) {
		this.respondedBy = respondedBy;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the contactNumber
	 */
	public String getContactNumber() {
		return contactNumber;
	}
	/**
	 * @param contactNumber the contactNumber to set
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
}
