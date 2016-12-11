package com.tnmnet.steel.entity;

import java.math.BigDecimal;

public interface IOrder {

	public BigDecimal getBuyPrice();

	/**
	 * 可以顺带卖吗？
	 * 
	 * @param salePrice
	 * @return
	 */
	public boolean canSale(BigDecimal salePrice);

	/**
	 * 卖出
	 * 
	 * @param times
	 * @param price
	 */
	public IOrder sale(String times, BigDecimal price);

	/**
	 * 买入金额
	 * 
	 * @return
	 */
	public BigDecimal getBuyAmt();

	/**
	 * 
	 * @return
	 */
	public BigDecimal getSalAmt();

	/**
	 * 
	 * @return
	 */
	public BigDecimal getSalAmtWithCost();

	/**
	 * 
	 * @param price
	 * @return
	 */
	public BigDecimal getProfitAmt();

	/**
	 * 
	 * @return
	 */
	public BigDecimal getProfitRate();
}