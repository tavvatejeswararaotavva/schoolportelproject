package com.school.model;

import org.springframework.security.core.GrantedAuthority;


public class Authority implements GrantedAuthority {

	private String authority;
	
	public Authority() {
		super();
	}

	public Authority(String role) {
		super();
		this.authority = role;
	}

	@Override
	public String getAuthority() {
		
		return authority;
	}

	
}
