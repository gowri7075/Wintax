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
public class Refer {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(name = "name", columnDefinition="VARCHAR(128)")
	private String name;
	
	@Column(name = "email", columnDefinition="VARCHAR(128)")
	private String email;
	
	@Column(name = "mobile_number", columnDefinition="VARCHAR(128)")
	private String mobileNumber;
	
	@Column(name = "referral_name", columnDefinition="VARCHAR(128)")
	private String referralName;
	
	@Column(name = "referral_email", columnDefinition="VARCHAR(128)")
	private String referralEmail;
	
	@Column(name = "referral_Mobile_Number", columnDefinition="VARCHAR(128)")
	private String referralMobileNumber;
	
	@Column(name = "message", columnDefinition="VARCHAR(128)")
	private String message;

	@Column(name = "responded_by", columnDefinition="VARCHAR(128)")
	private String respondedBy;
	
	@Column(name = "status", columnDefinition="VARCHAR(128)")
	private String status;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "referred_date")
	private Date referredDate;
	
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
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the referralName
	 */
	public String getReferralName() {
		return referralName;
	}

	/**
	 * @param referralName the referralName to set
	 */
	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	/**
	 * @return the referralEmail
	 */
	public String getReferralEmail() {
		return referralEmail;
	}

	/**
	 * @param referralEmail the referralEmail to set
	 */
	public void setReferralEmail(String referralEmail) {
		this.referralEmail = referralEmail;
	}

	/**
	 * @return the referralMobileNumber
	 */
	public String getReferralMobileNumber() {
		return referralMobileNumber;
	}

	/**
	 * @param referralMobileNumber the referralMobileNumber to set
	 */
	public void setReferralMobileNumber(String referralMobileNumber) {
		this.referralMobileNumber = referralMobileNumber;
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
	 * @return the referredDate
	 */
	public Date getReferredDate() {
		return referredDate;
	}

	/**
	 * @param referredDate the referredDate to set
	 */
	public void setReferredDate(Date referredDate) {
		this.referredDate = referredDate;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
