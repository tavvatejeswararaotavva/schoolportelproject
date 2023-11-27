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
@RequestMapping("/student")
@CrossOrigin("*")
public class StudentController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

//details
	@GetMapping("/")
	public User details(Principal principal) {
		return userRepository.findByEmail(principal.getName());
	}

// add student
	@PostMapping("/addStudent")
	public ResponseEntity<?> addStudent(@RequestBody User user, Principal principal) {
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin") || loginUser.getRole().equals("Teacher")) {
			user.setRole("Student");
			user.setFlag('S');
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.ok("Not accessible");
		}
	}

//student deleted
	@DeleteMapping("/delete/{email}")
	@Transactional
	public ResponseEntity<String> deleteStudent(@PathVariable String email, Principal principal) {
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin") || loginUser.getRole().equals("Teacher")) {
			userRepository.deleteByEmail(email);
			return ResponseEntity.ok("Student deleted !!");
		} else {
			return ResponseEntity.ok("Not accessible");
		}
	}

// list of student
	@GetMapping("/getAllStudent")
	public ResponseEntity<?> getAllStudent(Principal principal) {
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin") || loginUser.getRole().equals("Teacher")) {
			List<User> users = userRepository.findAll();
			List<User> std = new ArrayList<>();
			users.stream().forEach(user -> {
				if (user.getRole().equals("Student")) {
					std.add(user);
				}
			});
			return ResponseEntity.ok(std);
		} else {
			return ResponseEntity.ok("Not accessible");
		}
	}

//update student
	@PutMapping("/{email}")
	public ResponseEntity<?> updatedetails(@PathVariable String email, @RequestBody User user) {
		User dbuser = userRepository.findByEmail(email);
		if(dbuser.getRole().equals("Admin") || dbuser.getRole().equals("Teacher")) {
			return ResponseEntity.ok("Not accessible");
		}else {
			
		dbuser.setFirstName(user.getFirstName());
		dbuser.setLastName(user.getLastName());
		dbuser.setEmail(user.getEmail());
		dbuser.setPhoneNo(user.getPhoneNo());
		dbuser.setStandard(user.getStandard());
		dbuser.setSection(user.getSection());
		userRepository.save(user);
		return ResponseEntity.ok(dbuser);
		}
	}

}
