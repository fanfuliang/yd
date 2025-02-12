package com.yudao.lianying.v1.web.serviceImpl;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageConfig implements ErrorController {

	@Override
	public String getErrorPath() {
		return "index";
	}

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		return "index";
	}

}
