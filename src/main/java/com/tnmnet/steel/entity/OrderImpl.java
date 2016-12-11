package com.tnmnet.steel.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.tnmnet.steel.service.MiningCal;
import com.tnmnet.steel.util.DateUtil;
import com.tnmnet.steel.util.DateUtils;
import com.tnmnet.steel.util.GlobalConstant;

public class OrderImpl extends MiningCal implements IOrder {

	private String buyTimes;
	private BigDecimal buyPrice;
	private Integer qty;
	//
	private Date salTimes;
	private BigDecimal salPrice;

	public OrderImpl(String times, BigDecimal price, Integer qty) {
		super();
		this.buyTimes = times;
		this.buyPrice = price;
		this.qty = qty;
	}

	@Override
	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	@Override
	public boolean canSale(BigDecimal salePrice) {
		return moreThan(salePrice, buyPrice.multiply(new BigDecimal(1.02)));
	}

	@Override
	public IOrder sale(String times, BigDecimal price) {
		this.salTimes = DateUtil.getDateFromString(times, "yyyy-MM-dd");
		this.salPrice = price;
		return this;
	}

	@Override
	public BigDecimal getBuyAmt() {
		return buyPrice.multiply(new BigDecimal(qty));
	}

	@Override
	public BigDecimal getSalAmt() {
		return this.getSalAmt(this.salPrice);
	}

	private BigDecimal getSalAmt(BigDecimal price) {
		return price.multiply(new BigDecimal(qty));
	}

	public BigDecimal getProfitAmt() {
		return this.getProfitAmt(this.salPrice);
	}

	private BigDecimal getProfitAmt(BigDecimal salePrice) {
		BigDecimal costAmt = this.getBuyAmt().multiply(GlobalConstant.costRate.multiply(new BigDecimal(this.holdDays())));
		return this.getSalAmt(salePrice).subtract(this.getBuyAmt().add(costAmt));
	}

	@Override
	public BigDecimal getProfitRate() {
		return getProfitRate(this.salPrice);
	}

	private BigDecimal getProfitRate(BigDecimal salePrice) {
		BigDecimal priftAmt = this.getProfitAmt(salePrice);
		BigDecimal buyAmt = this.getBuyAmt();
		return priftAmt.subtract(buyAmt).divide(buyAmt, BigDecimal.ROUND_HALF_UP);
	}

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
		sb.append(" holdDays: " + this.holdDays());
		sb.append(" difPrice: " + this.salPrice.subtract(this.buyPrice));
		sb.append(" profiAmt: " + this.getProfitAmt());
		sb.append(" profRate: " + this.getProfitRate());
		//
		return sb.toString();
	}
}