package com.example.slackproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.slackproject.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String Home() {
		return "login/login";
	}
	
	@GetMapping("/login")
	public String Login(Model model, HttpServletRequest req) {
		// CSRF 토큰 꺼내기
		CsrfToken csrfToken = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
		// CSRF 토큰 model 객체에 담아 뷰로 전달하기
		model.addAttribute("_csrf", csrfToken);
		
		return "login/login";
	}
	
	@GetMapping("/join")
	public String Join() {
		return "login/join";
	}
	
	// 아이디 중복체크
	@PostMapping("/idCheck")
	@ResponseBody
	public String idCheck(@RequestParam String userId) {
		try {
			int res = userMapper.idCheck(userId);
			if(res > 0) {
				return "FALSE";
			}else {
				return "TRUE";
			}
		}catch(Exception e) {
			throw new RuntimeException("서비스 이용 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
		}
	}
	
	@PostMapping("/join")
	public String joinUser(HttpServletRequest req, @RequestParam Map<String, String> params) {
		try {
			// 비밀번호 암호화
			String userName = params.get("userName");
			String userId = params.get("userId");
			String userPw = params.get("userPw");
			String EncoderUserPw = passwordEncoder.encode(userPw);
			
			if(userName == null || userId == null || userPw == null || EncoderUserPw == null) {
				req.setAttribute("msg", "가입항목이 비어있어 회원가입에 실패하였습니다.");
				return "login/join";
			}
			
			Map<String, Object> searchParams = new HashMap<>();
			searchParams.put("userName", userName);
			searchParams.put("userId", userId);
			searchParams.put("userPw", EncoderUserPw);
			searchParams.put("userRoles", "USER");
			
			int res = userMapper.joinUser(searchParams);
			if(res > 0) {
				req.setAttribute("msg", "회원가입에 성공하였습니다.");
				return "login/login";
			}else {
				req.setAttribute("msg", "회원가입에 실패하였습니다.");
				return "login/join";
			}
		}catch(Exception e) {
	        throw new RuntimeException("서비스 이용 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
		}
	}
}
