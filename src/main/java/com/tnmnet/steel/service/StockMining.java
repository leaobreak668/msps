package com.tnmnet.steel.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.tnmnet.steel.entity.Order;
import com.tnmnet.steel.entity.Price;
import com.tnmnet.steel.util.Bdc;

public class StockMining extends MiningCal {
	//
	private BigDecimal initAmt = new BigDecimal(100000.0);// 初始金额
	private BigDecimal totalFundAmt = new BigDecimal(100000.0); // 可用资金额
	private BigDecimal totalUsedAmt = new BigDecimal(0);// 即时占用金额
	private BigDecimal totalMaxUsedAmt = new BigDecimal(0);// 最大占用金额
	private BigDecimal totalAmt = new BigDecimal(0);
	//
	private BigDecimal riseRate = new BigDecimal(0.03); // 追涨百分比
	private BigDecimal fallRate = new BigDecimal(0.05); // 杀跌百分比
	private BigDecimal layer = new BigDecimal(3); // 单数
	//
	private BigDecimal maxPrice = new BigDecimal(0);
	private BigDecimal minPrice = new BigDecimal(0);
	private BigDecimal latestPrice = new BigDecimal(0);

	private LinkedList<Order> stockList = new LinkedList<Order>();
	private List<Order> stockSaledList = new ArrayList<Order>();

	public void makeMoney(List<Price> lists) {
		for (Price price : lists) {
			String times = price.getTimes();
			BigDecimal currentPrice = price.getPrice();
			//
			if (stockList.size() == 0) {
				buyMinOrder(times, currentPrice);
				maxPrice = currentPrice;
				minPrice = currentPrice;
			} else if (Bdc.lt(currentPrice, minPrice, 2)) {
				if (buyMin(currentPrice)) {
					buyMinOrder(times, currentPrice);
				}
			} else if (Bdc.mt(currentPrice, maxPrice, 2)) {
				if (buyMax(currentPrice)) {
					buyMaxOrder(times, currentPrice);
				}
			} else if (stockList.size() >= 3) {//
				saleOrder(times, currentPrice);
			}
			latestPrice = currentPrice;
		}
		totalAmt = totalFundAmt;
		BigDecimal avgYearRate = new BigDecimal(0);
		for (Order item : stockList) {
			log("Hold.." + item.buyTimes + " # " + item.qty + " # " + item.buyPrice + " # " + item.getBuyAmt());
			totalAmt = totalAmt.add(item.getSalAmt(latestPrice));
			avgYearRate = avgYearRate.add(item.getYearRate(latestPrice));
		}
		log("totalFundAmt: " + totalFundAmt);
		log("totalAmt    : " + totalAmt);
		log("totalMaxUsedAmt: " + totalMaxUsedAmt);
		log("Rate: " + (totalAmt.subtract(initAmt)).divide(totalMaxUsedAmt, BigDecimal.ROUND_HALF_UP));
		//
		for (Order order : stockSaledList) {
			avgYearRate = avgYearRate.add(order.getYearRate());
		}
		log("avgYearRate: " + avgYearRate.divide(new BigDecimal(stockSaledList.size() + stockList.size()), BigDecimal.ROUND_HALF_UP));
	}

	//
	private void saleOrder(String times, BigDecimal currentPrice) {
		Order minOrder = stockList.element();
		if (cansale(currentPrice, minOrder)) {
			log("sale......." + times + " # " + currentPrice);
			//
			Order item = stockList.remove().sale(times, currentPrice);
			totalFundAmt = totalFundAmt.add(item.getSalAmt(currentPrice));
			totalUsedAmt = totalUsedAmt.subtract(item.getSalAmt(currentPrice));
			stockSaledList.add(item);
			//
			if (stockList.element().canSale(currentPrice)) {
				item = stockList.remove().sale(times, currentPrice);
				totalFundAmt = totalFundAmt.add(item.getSalAmt(currentPrice));
				totalUsedAmt = totalUsedAmt.subtract(item.getSalAmt(currentPrice));
				stockSaledList.add(item);
				//
				if (stockList.element().canSale(currentPrice)) {
					item = stockList.remove().sale(times, currentPrice);
					totalFundAmt = totalFundAmt.add(item.getSalAmt(currentPrice));
					totalUsedAmt = totalUsedAmt.subtract(item.getSalAmt(currentPrice));
					stockSaledList.add(item);
				}
			}
			//
			if (stockList.size() == 0) {
				maxPrice = new BigDecimal(0);
				minPrice = new BigDecimal(0);
			} else if (stockList.size() == 1) {
				minPrice = stockList.get(0).buyPrice;
				maxPrice = stockList.get(stockList.size() - 1).buyPrice;
			} else {
				minPrice = stockList.get(0).buyPrice;
			}
		}
	}

	private boolean cansale(BigDecimal currentPrice, Order order) {
		BigDecimal buyPrice = order.buyPrice;
		BigDecimal risePercent = currentPrice.subtract(buyPrice).divide(buyPrice, BigDecimal.ROUND_HALF_UP);
		return moreThan(risePercent, riseRate.multiply(layer));
	}

	//
	private boolean buyMin(BigDecimal currentPrice) {
		BigDecimal fallRates = minPrice.subtract(currentPrice).divide(minPrice, BigDecimal.ROUND_HALF_UP);
		return moreThan(fallRates, fallRate);
	}

	//
	private boolean buyMax(BigDecimal currentPrice) {
		return Bdc.mt(currentPrice.subtract(maxPrice).divide(maxPrice, BigDecimal.ROUND_HALF_UP), riseRate, 2);
	}

	//
	private Integer fallPercentFromMax(BigDecimal currentPrice) {
		if (eqZero(maxPrice)) {
			return 0;
		}
		return maxPrice.subtract(currentPrice).multiply(new BigDecimal("100")).divide(maxPrice, BigDecimal.ROUND_HALF_UP).intValue();
	}

	//
	private void buyMinOrder(String times, BigDecimal price) {
		Double qty = getQtyByPrice(price);
		if (qty == 0) {
			return;
		}
		Integer fallMax = fallPercentFromMax(minPrice);
		if (fallMax < (fallRate.multiply(new BigDecimal(stockList.size() - 1))).intValue()) {
			return;
		}
		//
		if (stockList.size() > 1) {
			qty = qty * (stockList.size() + 1) * 0.5; // 这句很关键
		}
		log("buy min.." + times + " # " + price);
		Order order = new Order(times, price, qty.intValue());
		stockList.add(order);
		//
		totalFundAmt = totalFundAmt.subtract(order.getBuyAmt());
		totalUsedAmt = totalUsedAmt.add(order.getBuyAmt());
		//
		if (lessThan(totalMaxUsedAmt, totalUsedAmt)) {
			totalMaxUsedAmt = totalUsedAmt;
		}
		minPrice = price;
	}

	//
	private void buyMaxOrder(String times, BigDecimal price) {
		Double qty = getQtyByPrice(price);
		if (qty == 0) {
			return;
		}
		log("buy max...." + times + " # " + price);
		Order order = new Order(times, price, qty.intValue());
		stockList.add(order);
		//
		totalFundAmt = totalFundAmt.subtract(order.getBuyAmt());
		totalUsedAmt = totalUsedAmt.add(order.getBuyAmt());
		//
		if (lessThan(totalMaxUsedAmt, totalUsedAmt)) {
			totalMaxUsedAmt = totalUsedAmt;
		}
		maxPrice = price;
	}

	//
	private Double getQtyByPrice(BigDecimal price) {
		Double qty = 0d;
		if (price.floatValue() <= 12) {
			qty = 300d;
		} else if (price.floatValue() > 12 && price.floatValue() <= 18) {
			qty = 200d;
		} else if (price.floatValue() > 18 && price.floatValue() <= 35) {
			qty = 100d;
		}
		return qty;
	}

	private void log(String s) {
		System.out.println(s);
	}
}
