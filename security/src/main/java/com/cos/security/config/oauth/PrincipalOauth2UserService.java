package com.cos.security.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest ClientRegistration : " + userRequest.getClientRegistration());
		// registrationId로 어떤 OAuth로 로그인했는지 알 수 있음.
		System.out.println("userRequest AccessToken: " + userRequest.getAccessToken());
		// 구글 로그인 버튼 클릭 -> 구글 로그인 화면 -> 로그인 완료 -> code를 리턴(OAuthClinet 라이브러리) -> AccessToken 요청 -> 여기까지가 userRequest 정보 
		// userRequest 정보를 통해서 회원 프로필 받아야함 -> loadUser(userRequest) 함수
		System.out.println("userRequest Attributes : " + super.loadUser(userRequest).getAttributes());
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		return oauth2User;
	}

}
