package com.tnmnet.steel.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.tnmnet.steel.service.MiningCal;
import com.tnmnet.steel.util.DateUtil;
import com.tnmnet.steel.util.DateUtils;

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
		return moreThan(salePrice, buyPrice.multiply(new BigDecimal(1.02)));
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
		int days = holdDays();
		return (new BigDecimal(365)).divide(new BigDecimal(days), BigDecimal.ROUND_HALF_UP, 6);
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
		return days > 0 ? days : 1;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append(" buyTimes: " + this.buyTimes);
		sb.append(" buyPrice: " + this.buyPrice);
		sb.append(" salTimes: " + DateUtils.getDateString(this.salTimes, "yyyy-MM-dd"));
		sb.append(" salPrice: " + this.salPrice);
		sb.append(" holdDays: " + holdDays());
		sb.append(" difPrice: " + this.salPrice.subtract(this.buyPrice));
		sb.append(" nowsRate : " + this.getSalAmt().subtract(this.getBuyAmt()).divide(this.getBuyAmt(), BigDecimal.ROUND_HALF_UP, 6));
		sb.append(" yearRate : " + this.getYearRate());
		sb.append(" difAmount: " + this.getSalAmt().subtract(this.getBuyAmt()));
		//
		return sb.toString();
	}
}