package com.school.controller;

import com.school.entities.JwtRequest;
import com.school.entities.JwtResponse;
import com.school.entities.User;
import com.school.repository.UserRepository;
import com.school.security.JwtUtil;
import com.school.security.UserDetailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	// Add User
	@PostMapping("/addUser")
	public ResponseEntity<User> create(@RequestBody User user) throws Exception {
		user.setRole("Admin");
		user.setFlag('A');
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return ResponseEntity.ok(userRepository.save(user));
	}

	// Show All User
	@GetMapping("/getAll")
	public List<User> getAllUser(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader("Authorization");
		System.out.println("I am in get all " + token);
		return userRepository.findAll();
	}

	// delete Admin
	@DeleteMapping("/delete/{email}")
	public ResponseEntity<String> deleteStudent(@PathVariable String email, Principal principal) {
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin")) {
			userRepository.deleteByEmail(email);
			return ResponseEntity.ok("Admin deleted !!");
		} else {
			return ResponseEntity.ok("Not accessible");
		}
	}

	// update User
	@PutMapping("/update/{email}")
	public ResponseEntity<?> updateUserDetails(@RequestBody User user, @PathVariable String email,
			Principal principal) {
		User dbuser = userRepository.findByEmail(email);
		User loginUser = userRepository.findByEmail(principal.getName());
		if (loginUser.getRole().equals("Admin")) {
			dbuser.setFirstName(user.getFirstName());
			dbuser.setLastName(user.getLastName());
			dbuser.setEmail(user.getEmail());
			dbuser.setPhoneNo(user.getPhoneNo());
			dbuser.setStandard(user.getStandard());
			dbuser.setSection(user.getSection());
			dbuser.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(dbuser);
			return ResponseEntity.ok(dbuser);
		} else {
			return ResponseEntity.ok("not accessible");
		}
	}

	// login user
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest request) throws Exception {
		try {
			authenticate(request.getEmail(), request.getPassword());
		} catch (UsernameNotFoundException exc) {
			exc.printStackTrace();
			throw new Exception("controller: User not found");
		}
		UserDetails user = userDetailService.loadUserByUsername(request.getEmail());
		String token = jwtUtil.generateToken(user.getUsername());
		// System.out.println(token);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	public void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

		} catch (DisabledException e) {
			throw new Exception("user disabled " + e.getMessage());
		}

		catch (BadCredentialsException e) {
			throw new Exception("Invalid credentials " + e.getMessage());

		}
	}

	// login user
	@GetMapping("/currentUserRole")
	public Collection<? extends GrantedAuthority> getUser(Principal principal) {
		System.out.println("getuser");
		User user = (User) userDetailService.loadUserByUsername(principal.getName());
		return user.getAuthorities();
	}

	// admin detail
	@GetMapping("/")
	public User detail(Principal principal) {
		return (User) userDetailService.loadUserByUsername(principal.getName());
	}

	@GetMapping("/emails")
	public List<String> sendEmails(@RequestParam String standard, @RequestParam(required = false) String section) {
		if (standard != null && section != null) {
			return userRepository.findEmailByStandardAndSection(standard, section);
		} else if (section == null) {
			return userRepository.findEmailByStandard(standard);
		} else {
			return null;
		}
	}
}