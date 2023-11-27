package com.school.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.entities.User;
import com.school.repository.UserRepository;

import jakarta.transaction.Transactional;


@RestController
@RequestMapping("/teacher")
@CrossOrigin("*")
public class TeacherController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
//detail
	@GetMapping("/")
	public User details(Principal principal) {
		return userRepository.findByEmail(principal.getName());
	}

// add teacher
	@PostMapping("/addTeacher")
	public ResponseEntity<?> addStudent(@RequestBody User user, Principal principal){
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin")) {
			user.setRole("Teacher");
			user.setFlag('T');
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.ok("Not accessible");
		}
	}
	
// Show teacher
	@GetMapping("/getAllTeacher")
	public ResponseEntity<?> ShowStd(Principal principal){
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin")) {
			List<User> users = userRepository.findAll();
			List<User> teacher = new ArrayList<>();
			users.stream().forEach(user -> {
				if (user.getRole().equals("Teacher")) {
					teacher.add(user);
				}
			});
			return ResponseEntity.ok(teacher);
		} else {
			return ResponseEntity.ok("Not accessible");
		}
	}
	
//update teacher
	@PutMapping("/update/{email}")
	public ResponseEntity<?> updateStd(@RequestBody User user, @PathVariable String email, Principal principal){
		User dbUser = userRepository.findByEmail(email);
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin") || loginUser.getRole().equals("Teacher")) {
		dbUser.setFirstName(user.getFirstName());
		dbUser.setLastName(user.getLastName());
		dbUser.setPassword(user.getPassword());
		dbUser.setStandard(user.getStandard());
		dbUser.setSection(user.getSection());
		dbUser.setPhoneNo(user.getPhoneNo());
		dbUser.setRollNo(user.getRollNo());
		userRepository.save(dbUser);
		return ResponseEntity.ok(dbUser);
		}else {
			return ResponseEntity.ok("Not accessible");
		}
	}
	
//Delete Teacher
	@DeleteMapping("/delete/{email}")
	@Transactional
	public ResponseEntity<String> deleteTeacher(@PathVariable String email, Principal principal){
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin")) {
			userRepository.deleteByEmail(email);
			return ResponseEntity.ok("Teacher deleted !!");
		} else {
			return ResponseEntity.ok("Not accessible");
		}
	}
}


