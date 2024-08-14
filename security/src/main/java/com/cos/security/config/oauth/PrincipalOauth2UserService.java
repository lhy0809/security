package com.cos.security.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security.config.auth.PrincipalDetails;
import com.cos.security.config.oauth.provider.FacebookUserInfo;
import com.cos.security.config.oauth.provider.GoogleUserInfo;
import com.cos.security.config.oauth.provider.OAuth2UserInfo;
import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	@Autowired
	private UserRepository userRepository;
	
	// (1) 구글로부터 받은 userRequest를 후처리 하는 함수.
	// (2) @AuthenticationPrincipal 어노테이션이 생성된다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest ClientRegistration : " + userRequest.getClientRegistration());
		// registrationId로 어떤 OAuth로 로그인했는지 알 수 있음.
		System.out.println("userRequest AccessToken: " + userRequest.getAccessToken());
		// 구글 로그인 버튼 클릭 -> 구글 로그인 화면 -> 로그인 완료 -> code를 리턴(OAuthClinet 라이브러리) -> AccessToken 요청 -> 여기까지가 userRequest 정보 
		// userRequest 정보를 통해서 회원 프로필 받아야함 -> loadUser(userRequest) 함수
		System.out.println("userRequest Attributes : " + super.loadUser(userRequest).getAttributes()); // Map으로 나옵니다.
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		OAuth2UserInfo oauth2UserInfo = null;
		String provider = userRequest.getClientRegistration().getClientName(); // google
		
		if(provider.equalsIgnoreCase("google")) {
			oauth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}else if(provider.equalsIgnoreCase("facebook")) {
			oauth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}else {
			System.out.println("ㅎㅎㅎ");
		}
		
		String providerId = oauth2UserInfo.getProviderId();
		String username = provider+"_"+providerId;
//		String password = bCryptPasswordEncoder.encode("security");
		String role = "ROLE_USER";
		String email = oauth2UserInfo.getEmail();
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
//					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}else {
			
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}

}
