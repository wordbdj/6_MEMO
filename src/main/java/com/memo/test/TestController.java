package com.memo.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.memo.post.mapper.PostMapper;

@Controller
public class TestController {

	@Autowired
	PostMapper postMapper;
	
	@ResponseBody
	@GetMapping("/test1")
	public String test1( ) {
		return "Hello World!";
	}
	
	@ResponseBody
	@GetMapping("/test2")
	public Map<String, Object> test2() {
		Map<String, Object> map = new HashMap<>();
		map.put("a", 111);
		map.put("b", 111);
		map.put("c", 222);
		
		return map;
	}
	
	@GetMapping("/test3")
	public String test3( ) {
		return "test/test3";
	}
	
	@ResponseBody
	@GetMapping("/test4")
	public List<Map<String, Object>> test4() {
		return postMapper.selectPostListTest();
	}
	
	
	@GetMapping("/test5")
	public String test5() {
		return "memo/Login";
	}
	
}
