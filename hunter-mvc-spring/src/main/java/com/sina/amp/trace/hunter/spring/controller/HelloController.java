package com.sina.amp.trace.hunter.spring.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sina.amp.trace.hunter.spring.service.ServiceA;
import com.sina.amp.trace.hunter.spring.utils.ResponseJson;
import com.sina.amp.trace.hunter.spring.utils.StatusCode;


@Controller
@RequestMapping("/index")
public class HelloController {
	Logger LOG = LoggerFactory.getLogger(HelloController.class);
	
	@Autowired
	ServiceA serviceA;
	
	@ResponseBody
	@RequestMapping("/hello")
	public ResponseJson hello() {
		LOG.info("hello begin");
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		serviceA.method1();
		ResponseJson response = new ResponseJson();
		response.setStatus(StatusCode.OPERATE_SUCCESS);
		response.addData("hello", "wuqiang");
		LOG.info("hello begin");
		return response;
	}
	
	
	@ResponseBody
	@RequestMapping("/happy")
	public ResponseJson happy() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serviceA.method2();
		ResponseJson response = new ResponseJson();
		response.setStatus(StatusCode.OPERATE_SUCCESS);
		response.addData("happy", "wuqiang");
		return response;
	}
}
