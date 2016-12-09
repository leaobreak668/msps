package com.tnmnet.steel.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PRICE")
public class Price {
	@Id
	private String id;
	private String code;
	private String times;
	private BigDecimal price;

	public Price(String code, String times, BigDecimal price) {
		super();
		this.code = code;
		this.times = times;
		this.price = price;
	}

	public Price() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return times + " - " + this.price.toPlainString();
	}

}
