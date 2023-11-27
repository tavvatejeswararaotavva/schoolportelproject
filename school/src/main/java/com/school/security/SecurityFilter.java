package com.school.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil util;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. read token from Auth header
		String token=request.getHeader("Authorization");
	    if(token!=null ) {
	    	//do validation
	    	String username = util.getUsername(token);
	    	
	    	//username should not be empty, context-auth must be empty
	    	if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
	    		
	    		UserDetails user=userDetailsService.loadUserByUsername(username);
	    
	    	    //validate token
	    		boolean isValid=util.validateToken(token, user.getUsername());
	    		System.out.println("hello  "+token);
	            if(isValid) {
	            	UsernamePasswordAuthenticationToken authenticationToken= 
	            			new UsernamePasswordAuthenticationToken(username, user.getPassword(),user.getAuthorities());
	            	
	            	authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            	
	            	//final object stored in SecurityContext with User Details(un,pwd)
	            	SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            }
	    	
	    	}
	    }
    	filterChain.doFilter(request, response);

	
	}

}