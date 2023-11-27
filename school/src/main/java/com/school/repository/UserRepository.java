package com.school.repository;

import com.school.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findById(int id);
	public User findByEmail(String email);
	public void deleteByEmail(String email);
	@Query("select u.email from User u where u.standard = ?1 and u.section = ?2")
	public ArrayList<String> findEmailByStandardAndSection(String standard, String section);
	@Query("select u.email from User u where u.standard = ?1")
	List<String> findEmailByStandard(String standard);
}
