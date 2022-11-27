package com.account.tax.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.account.tax.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);

	List<User> findAllByOrderByIdDesc();

	@Query("from User u join u.roles r where r.name='ROLE_ADMIN'")
	List<User> findAllAdminUsers();
	
	@Query("from User u join u.roles r where r.name='ROLE_USER' order by u.id desc")
	List<User> findAllUsers();
	
	 User findByResetToken(String resetToken);
}
