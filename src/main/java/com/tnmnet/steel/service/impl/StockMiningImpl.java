package com.tnmnet.steel.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.tnmnet.steel.entity.IOrder;
import com.tnmnet.steel.entity.OrderImpl;
import com.tnmnet.steel.entity.Price;
import com.tnmnet.steel.service.AbsStockMining;
import com.tnmnet.steel.util.Bdc;

public class StockMiningImpl extends AbsStockMining {
	//
	private BigDecimal initAmt = new BigDecimal(1000000.0);// 初始金额
	private BigDecimal totalAmt = new BigDecimal(1000000.0); // 可用资金额
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

	private LinkedList<IOrder> stockList = new LinkedList<IOrder>();
	private List<IOrder> stockSaledList = new ArrayList<IOrder>();

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
		for (IOrder item : stockList) {
			item.sale(latestTimes, latestPrice);
			totalAmt = totalAmt.add(item.getSalAmtWithCost());
			stockSaledList.add(item);
			log("Sale Hold.." + item);
		}
		//
		log("totalAmt    : " + totalAmt);
		log("totalMaxUsedAmt: " + totalMaxUsedAmt);
		log("Rate: " + (totalAmt.subtract(initAmt)).divide(totalMaxUsedAmt, BigDecimal.ROUND_HALF_UP));
	}

	/**
	 * 可以卖的最低买入单
	 * 
	 * @param salePrice
	 * @return
	 */
	private boolean canSaleMin(IOrder order, BigDecimal currentPrice) {
		BigDecimal risePercent = currentPrice.subtract(order.getBuyPrice()).divide(order.getBuyPrice(), BigDecimal.ROUND_HALF_UP);
		return moreThan(risePercent, riseRate.multiply(layer));
	}

	//
	private void saleOrder(String times, BigDecimal currentPrice) {
		//
		while (stockList.size() > 0 && stockList.getFirst().canSale(currentPrice)) {
			log("sale......." + times + " # " + currentPrice);
			IOrder item = stockList.remove().sale(times, currentPrice);
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
		log("buy min.." + times + " # " + price);
		IOrder order = new OrderImpl(times, price, qty.intValue());
		stockList.addFirst(order);
		//
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
		IOrder order = new OrderImpl(times, price, qty.intValue());
		stockList.addLast(order);
		//
		totalUsedAmt = totalUsedAmt.add(order.getBuyAmt());
		//
		if (lessThan(totalMaxUsedAmt, totalUsedAmt)) {
			totalMaxUsedAmt = totalUsedAmt;
		}
		maxPrice = price;
	}
}
