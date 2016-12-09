package com.tnmnet.steel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnmnet.steel.entity.Price;
import com.tnmnet.steel.service.PriceService;
import com.tnmnet.steel.service.StockMining;

@RestController
public class HelloWorldController {

	@Autowired
	private PriceService priceService;

	@RequestMapping("/hello/{code}")
	public String index(@PathVariable("code") String code) {
		List<Price> lists = priceService.findAllByCode(code);
		StockMining sm = new StockMining();
		sm.makeMoney(lists);
		return "Hello Spring boot Service!" + lists.size();
	}
}