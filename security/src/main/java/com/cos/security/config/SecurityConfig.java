package com.cos.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
public class SecurityConfig /*extends WebSecurityConfigurerAdapter 구 버전에서 사용*/{
	
	// @Bean 어노테이션은 해당 메서드의 리턴되는 오브젝트를 IoC 해준다.
	@Bean
	public BCryptPasswordEncoder endcodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.authorizeRequests(authorize -> authorize
				.requestMatchers(new AntPathRequestMatcher("/user/**")).authenticated() // /user > 인증 필요
				.requestMatchers(new AntPathRequestMatcher("/manager/**")).hasAnyRole("MANAGER", "ADMIN") // /manager > ROLE_MANAGER, ROLE_ADMIN 권한 필요
				.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN") // /admin > ROLE_ADMIN 권한 필요
				.anyRequest().permitAll() // 나머지 url > 권한 허용.
				);

		http.formLogin(form -> form
				.loginPage("/loginForm")
					// /user /manager /admin 으로 접근 시 403 error 발생 
					// 단, formLogin.loginPage 를 지정하면 권한이 필요한 url에 대해서 /login 으로 이동.
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/"));
					// login 주소가 호출이 되면 security가 가로채서 로그인을 진행한다.
					// 	> controller에 /login api 를 만들지 않아도 된다.
					// 성공 후 defaultSuccessUrl로 이동한다.
					// 	> 여기서 defaultSuccessUrl은 호출한 페이지이다. /user로 호출하면 /user로 가고, /로 호출하면 /로 간다.
		http.logout().permitAll();
		return http.build();
	}
	
}
