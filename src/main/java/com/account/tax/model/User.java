package com.account.tax.model;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String password;
    @CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;
    
    @Column(name = "reset_token")
	private String resetToken;


    /**
	 * @return the resetToken
	 */
	public String getResetToken() {
		return resetToken;
	}

	/**
	 * @param resetToken the resetToken to set
	 */
	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"))
    private Collection < Role > roles;

    public User() {}

    public User(String name,String lastName, String mobileNumber, String email, String password,Date createdDate) {
        this.name = name;
        this.lastName=lastName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.createdDate=createdDate;
    }

    public User(String name,String lastName, String mobileNumber, String email,Date createdDate, String password, Collection < Role > roles) {
        this.name = name;
        this.lastName=lastName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.createdDate=createdDate;
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
	 * @return the name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param name the name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public Collection < Role > getRoles() {
        return roles;
    }

    public void setRoles(Collection < Role > roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", lastName='" + lastName + '\'' +
            ", mobileNumber='" + mobileNumber + '\'' +
            ", email='" + email + '\'' +
            ", createdDate='" + createdDate + '\'' +
            ", password='" + "*********" + '\'' +
            ", resetToken='" + resetToken + '\'' +
            ", roles=" + roles +
            '}';
    }
}