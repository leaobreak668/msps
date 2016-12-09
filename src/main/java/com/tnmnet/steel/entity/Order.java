package com.tnmnet.steel.entity;

import java.math.BigDecimal;

public class Order {

	private String times;
	private BigDecimal price;
	private Integer qty;

	public Order(String times, BigDecimal price, Integer qty) {
		super();
		this.times = times;
		this.price = price;
		this.qty = qty;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getAmt() {
		return price.multiply(new BigDecimal(qty));
	}

	public BigDecimal getCurAmt(BigDecimal price) {
		return price.multiply(new BigDecimal(qty));
	}
}