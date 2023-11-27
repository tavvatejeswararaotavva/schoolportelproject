package com.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.entities.Screen;



public interface ScreenRepository extends JpaRepository<Screen, Integer> {
	
	public Screen findByName(String name);
}

