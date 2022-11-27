package com.account.tax.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.tax.model.Refer;

public interface ReferRepo extends JpaRepository<Refer, Integer>{

	List<Refer> findAllByOrderByIdDesc();

}
