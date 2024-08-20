package com.xi.fmcs.config.security.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class PrincipalDetails implements UserDetails{
	
	private AdminMemberLoginDto adminMemberLoginDto;
	
	public PrincipalDetails(AdminMemberLoginDto adminMemberLoginDto) {
		this.adminMemberLoginDto = adminMemberLoginDto;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + Integer.toString(adminMemberLoginDto.getGradeType())));
		return authorities;
	}

	@Override
	public String getPassword() {
		return adminMemberLoginDto.getPwd();
	}

	@Override
	public String getUsername() {
		return adminMemberLoginDto.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
