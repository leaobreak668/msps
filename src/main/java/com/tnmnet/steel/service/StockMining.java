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
	private BigDecimal initAmt = new BigDecimal(1000000.0);// 初始金额
	private BigDecimal totalFundAmt = new BigDecimal(1000000.0); // 可用资金额
	private BigDecimal totalUsedAmt = new BigDecimal(0);// 即时占用金额
	private BigDecimal totalMaxUsedAmt = new BigDecimal(0);// 最大占用金额
	//
	private BigDecimal riseRate = new BigDecimal(0.03); // 追涨百分比
	private BigDecimal fallRate = new BigDecimal(0.09); // 杀跌百分比
	private BigDecimal layer = new BigDecimal(3); // 卖出追涨的位数
	//
	private BigDecimal maxPrice = new BigDecimal(0);
	private BigDecimal minPrice = new BigDecimal(0);
	private BigDecimal latestPrice = new BigDecimal(0);
	private String latestTimes;

	private LinkedList<Order> stockList = new LinkedList<Order>();
	private List<Order> stockSaledList = new ArrayList<Order>();

	public void makeMoney(List<Price> lists) {
		for (Price price : lists) {
			String times = price.getTimes();
			BigDecimal currentPrice = price.getPrice();
			//
			latestPrice = currentPrice;
			latestTimes = times;
			//
			if (stockList.size() == 0) {
				buyMinOrder(times, currentPrice);
				maxPrice = currentPrice;
				minPrice = currentPrice;
				continue;
				//
			} else if (canSaleMin(stockList.getFirst(), currentPrice)) {
				saleOrder(times, currentPrice);
				continue;
			} else if (lessThan(currentPrice, minPrice)) {
				if (buyMin(currentPrice)) {
					buyMinOrder(times, currentPrice);
				}
				continue;
			} else if (moreThan(currentPrice, maxPrice)) {
				if (buyMax(currentPrice)) {
					buyMaxOrder(times, currentPrice);
					continue;
				}
			}
		}
		log("totalFundAmt: " + totalFundAmt);
		BigDecimal totalAmt = new BigDecimal(totalFundAmt.toPlainString());
		for (Order item : stockList) {
			item.sale(latestTimes, latestPrice);
			totalAmt = totalAmt.add(item.getSalAmt());
			stockSaledList.add(item);
			log("Sale Hold.." + item.getBuyPrice() + " - " + latestPrice + " # " + item.getYearRate());
		}
		//
		log("totalAmt    : " + totalAmt);
		log("totalMaxUsedAmt: " + totalMaxUsedAmt);
		log("Rate: " + (totalAmt.subtract(initAmt)).divide(totalMaxUsedAmt, BigDecimal.ROUND_HALF_UP));
		//
		BigDecimal avgYearRate = new BigDecimal("0");
		for (Order order : stockSaledList) {
			avgYearRate = avgYearRate.add(order.getYearRate());
		}
		log("avgYearRate: " + avgYearRate.divide(new BigDecimal(stockSaledList.size()), BigDecimal.ROUND_HALF_UP));
	}

	/**
	 * 可以卖的最低买入单
	 * 
	 * @param salePrice
	 * @return
	 */
	private boolean canSaleMin(Order order, BigDecimal currentPrice) {
		BigDecimal risePercent = currentPrice.subtract(order.getBuyPrice()).divide(order.getBuyPrice(), BigDecimal.ROUND_HALF_UP);
		return moreThan(risePercent, riseRate.multiply(layer));
	}

	//
	private void saleOrder(String times, BigDecimal currentPrice) {
		//
		while (stockList.size() > 0 && stockList.getFirst().canSale(currentPrice)) {
			log("sale......." + times + " # " + currentPrice);
			Order item = stockList.remove().sale(times, currentPrice);
			totalFundAmt = totalFundAmt.add(item.getSalAmt());
			totalUsedAmt = totalUsedAmt.subtract(item.getSalAmt());
			stockSaledList.add(item);
		}
		//
		if (stockList.size() == 0) {
			maxPrice = new BigDecimal(0);
			minPrice = new BigDecimal(0);
		} else if (stockList.size() == 1) {
			minPrice = stockList.get(0).getBuyPrice();
			maxPrice = stockList.get(stockList.size() - 1).getBuyPrice();
		} else {
			minPrice = stockList.get(0).getBuyPrice();
		}
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
	private BigDecimal fallPercentFromMax(BigDecimal currentPrice) {
		if (eqZero(maxPrice)) {
			return new BigDecimal(0);
		}
		return maxPrice.subtract(currentPrice).divide(maxPrice, BigDecimal.ROUND_HALF_UP);
	}

	//
	private void buyMinOrder(String times, BigDecimal price) {
		Double qty = getQtyByPrice(price);
		if (qty == 0) {
			return;
		}
		BigDecimal fallMax = fallPercentFromMax(price);
		if (lessThan(fallMax, fallRate.multiply(new BigDecimal(stockList.size() - 1)))) {
			return;
		}
		//
		if (stockList.size() > 1) {
			qty = qty * (stockList.size() + 1) * 0.5; // 这句很关键
		}
		log("buy min.." + times + " # " + price);
		Order order = new Order(times, price, qty.intValue());
		stockList.addFirst(order);
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
		stockList.addLast(order);
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
