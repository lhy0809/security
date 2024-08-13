package com.cos.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

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
	@GetMapping("/login")
	@ResponseBody
	public String login() {
		return "login";
	}
	
	@GetMapping("/join")
	@ResponseBody
	public String join() {
		return "join";
	}
	
	@GetMapping("/joinProc")
	@ResponseBody
	public String joinProc() {
		return "joinProc";
	}
}
