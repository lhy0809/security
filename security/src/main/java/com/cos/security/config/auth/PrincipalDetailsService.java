package com.cos.security.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;

// loginProcessingUrl("/login") : 시큐리티 설정에서 loginProcessingUrl 가 호출되면
// @Service PrincipalDetailsService : UserDetailsService 타입으로 IoC 되어 있는 클래스의  
// loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	// (1) 시큐리티 session = Authentication Type = UserDetails Type
	// UserDetails 타입인 PrincipalDetails(user) 가 리턴되면 
	// Authentication(UserDetails) 가 되고
	// 이게 시큐리티 session(내부 Authentication(내부 UserDetails)) 가 된다.
	
	// (2) @AuthenticationPrincipal 어노테이션이 생성된다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(username);
		User user = userRepository.findByUsername(username);
		if(user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}

}
