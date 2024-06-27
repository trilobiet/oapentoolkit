package com.trilobiet.oapen.oapentoolkit.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController  {

	// Catches everything we can not explain via GlobalExceptionHandler 
	// (like thymeleaf rendering errors)
	
    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "error/othererror";
    }
}