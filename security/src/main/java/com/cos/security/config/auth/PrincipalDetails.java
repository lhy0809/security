package com.cos.security.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security.model.User;

import lombok.Data;

// security 가 loginProcessingUrl 로 호출이 되면 요청을 가로채서 로그인을 진행시킨다.
// 로그인이 완료되면 > session 을 생성한다. > security 가 제공하는 session 을 사용한다. (Security ContextHolder 라는 세션)
// 이 Security ContextHolder 에 들어갈 수 있는 오브젝트는 Authentication 타입 객체
// Authentication 객체 안에는 User 정보가 있어야 됨.
// 이 User 정보를 담고 있는 User 오브젝트의 타입 > UesrDetails 타입 객체 

// Security ContextHolder > Authentication 객체 > UserDetails 객체 
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
	
	private User user;
	private Map<String, Object> attributes;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	// GrantedAuthority 타입으 반환하는 이 함수는 
	// 해당 User의 권한을 리턴하는 곳
	// user의 권한은 role에 있는데, String 타입이라 바로 리턴할 수 없음.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				return user.getRole();
			}
			
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return (String) attributes.get("sub");
	}

}
