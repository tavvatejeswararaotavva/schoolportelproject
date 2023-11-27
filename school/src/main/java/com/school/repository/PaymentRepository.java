package com.school.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.school.entities.Payments;


public interface PaymentRepository extends JpaRepository<Payments, Long> {
	public  List<Payments> findAll();

}
