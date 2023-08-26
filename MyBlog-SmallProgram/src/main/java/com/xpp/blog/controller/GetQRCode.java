package com.xpp.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetQRCode {
	@GetMapping("getQRCode")
	public Map<String,String > getQRCode(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("url", "https://i.loli.net/2020/04/29/ZSrBRjAVEYXndCL.png");
		return map;
		}
	}
