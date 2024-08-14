package com.cos.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security.model.User;

// CRUD 함수를 JpaRepository 가 들고 있음.
// @Repository 어노테이션이 없어도 JpaRepository를 상속했기 때문에 IoC 된다.
public interface UserRepository extends JpaRepository<User, Integer> {
	// findBy 는 고정
	// select * from user where username = ?
	public User findByUsername(String username); // JPA Query methods 문법 확인
}
