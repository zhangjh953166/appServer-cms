package com.sanmao.server.cms.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanmao.server.model.Order;
import com.sanmao.server.service.OrderService;

@Controller
public class DubboController {
	
	@Reference(version = "1.0.0")
	private OrderService service;
	
	@RequestMapping(value="/orderGW.html", method = RequestMethod.GET)
	@ResponseBody
	public String order(){
		List<Order> list = service.query(null);
		return ""+list.get(0).getAmount();
	}
}
