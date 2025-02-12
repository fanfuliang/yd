package com.yudao.lianying.v1.web.serviceImpl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageControl {

	@RequestMapping(value = "/")
	public String main() {
		return "index";
	}

	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}
}
