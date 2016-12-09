package com.tnmnet.steel.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.tnmnet.steel.service.MiningCal;
import com.tnmnet.steel.util.DateUtil;

public class Order extends MiningCal {

	private String buyTimes;
	private BigDecimal buyPrice;
	private Integer qty;
	//
	private Date salTimes;
	private BigDecimal salPrice;

	public Order(String times, BigDecimal price, Integer qty) {
		super();
		this.buyTimes = times;
		this.buyPrice = price;
		this.qty = qty;
	}

	public String getBuyTimes() {
		return buyTimes;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public Integer getQty() {
		return qty;
	}

	/**
	 * 可以顺带卖吗？
	 * 
	 * @param salePrice
	 * @return
	 */
	public boolean canSale(BigDecimal salePrice) {
		return moreThan(salePrice, buyPrice.multiply(new BigDecimal(1.1)));
	}

	/**
	 * 可以卖的最低买入单
	 * 
	 * @param salePrice
	 * @return
	 */
	public boolean canSaleMin(BigDecimal salePrice, BigDecimal riseRate, BigDecimal layer) {
		BigDecimal risePercent = salePrice.subtract(buyPrice).divide(buyPrice, BigDecimal.ROUND_HALF_UP);
		return moreThan(risePercent, riseRate.multiply(layer));
	}

	/**
	 * 卖出
	 * 
	 * @param times
	 * @param price
	 */
	public Order sale(String times, BigDecimal price) {
		this.salTimes = DateUtil.getDateFromString(times, "yyyy-MM-dd");
		this.salPrice = price;
		return this;
	}

	/**
	 * 买入金额
	 * 
	 * @return
	 */
	public BigDecimal getBuyAmt() {
		return buyPrice.multiply(new BigDecimal(qty));
	}

	/**
	 * 卖出金额
	 * 
	 * @return
	 */
	public BigDecimal getSalAmt() {
		return salPrice.multiply(new BigDecimal(qty));
	}

	public BigDecimal getSalAmt(BigDecimal price) {
		return price.multiply(new BigDecimal(qty));
	}

	public BigDecimal getYearRate() {
		return getYearRate(this.salPrice);
	}

	public BigDecimal getYearRate(BigDecimal salePrice) {
		BigDecimal saleAmt = this.getSalAmt(salePrice);
		BigDecimal buyAmt = this.getBuyAmt();
		BigDecimal annual = this.annual();
		return saleAmt.subtract(buyAmt).divide(buyAmt, BigDecimal.ROUND_HALF_UP).multiply(annual);
	}

	/**
	 * 年化计算
	 * 
	 * @return
	 */
	private BigDecimal annual() {
		int days = holdDays() * 100;
		return new BigDecimal(days).divide(new BigDecimal(365), BigDecimal.ROUND_HALF_UP, 6);
	}

	/**
	 * 持有天数
	 * 
	 * @return
	 */
	private int holdDays() {
		if (this.salTimes == null) {
			System.out.println("");
		}
		Date saleDate = this.salTimes == null ? Calendar.getInstance().getTime() : this.salTimes;
		int days = this.calDays(DateUtil.getDateFromString(this.buyTimes, "yyyy-MM-dd"), saleDate);
		return days;
	}
}