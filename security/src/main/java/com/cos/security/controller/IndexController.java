package com.cos.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security.config.auth.PrincipalDetails;
import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	@ResponseBody
	public String testLogin(Authentication authentication) {
		System.out.println("/test/login ========================== ");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : " + principalDetails.getUser());
		return "세션 정보 확인하기1";
	}
	
	@GetMapping("/test/login2")
	@ResponseBody
	public String testLogin2(@AuthenticationPrincipal UserDetails userDetails) {
		System.out.println("/test/login2 ========================== ");
		System.out.println("userDetails : " + userDetails.getUsername());
		return "세션 정보 확인하기2";
	}
	
	@GetMapping("/test/login3")
	@ResponseBody
	public String testLogin3(@AuthenticationPrincipal PrincipalDetails userDetails) {
		// UserDetails 타입으로 PrincipalDetails를 선언했기 때문에 변경
		System.out.println("/test/login2 ========================== ");
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션 정보 확인하기3";
	}
	
	@GetMapping("/test/oauth/login")
	@ResponseBody
	public String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauthUser) {
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("/test/oauth/login ========================== ");
		System.out.println("oAuth2User : " + oAuth2User.getAttributes());
		System.out.println("oauthUser : " + oauthUser.getAttributes());
		return "google 세션 정보 확인";
	}
	
	/** 정리
	 * 1. 스프링 시큐리티 > 시큐리티 세션이 있다. > 시큐리티 세션에는 Authentication 객체가 들어갈 수 있음
	 * 2. Authentication 객체는 UserDetails 타입, OAuth2User 타입이 들어갈 수 있다.
	 * 3. 그렇다면, UserDetails 과 OAuth2User 을 모두 implements 받은 클래스를 생성하여 사용하면 된다.
	 * @return
	 */
	
	@GetMapping({"","/"})
	public String index() {
		return "index";
	}
	
	@GetMapping("/user")
	@ResponseBody
	public String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	@ResponseBody
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	@ResponseBody
	public String manager() {
		return "manager";
	}
	
	/**
	 * spring security 가 해당 주소를 가로챈다.
	 * @return
	 */
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		// password 암호화가 없으면 security 로그인을 할 수 없음
		String encryptPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encryptPassword);
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	@ResponseBody
	public String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	@ResponseBody
	public String data() {
		return "데이터정보";
	}
}
