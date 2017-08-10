package com.sanmao.server.cms.web;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sanmao.server.model.Order;
import com.sanmao.server.service.OrderService;

@Controller
public class DubboController {
	
	@Reference(version = "1.0.1",check=true)
	private OrderService service;
	
	private AtomicInteger count = new AtomicInteger(0);
	
	@RequestMapping(value="/orderGW.html", method = RequestMethod.GET)
	@ResponseBody
	public String order(){
		List<Order> list = service.query(null);
		
		System.out.println(count.incrementAndGet());
		return ""+list.get(0).getAmount();
	}
}
