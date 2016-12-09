package com.tnmnet.steel.service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.tnmnet.steel.entity.Order;
import com.tnmnet.steel.entity.Price;
import com.tnmnet.steel.util.Bdc;

public class StockMining {
	private BigDecimal initAmt = new BigDecimal(100000.0);
	private BigDecimal totalAmt = initAmt;
	private BigDecimal totalMinAmt = initAmt;
	//
	private BigDecimal rate = new BigDecimal(5);
	private BigDecimal layer = new BigDecimal(2);
	//
	//
	private BigDecimal maxPrice = new BigDecimal(0);
	private BigDecimal minPrice = new BigDecimal(0);
	private BigDecimal latestPrice = new BigDecimal(0);
	private LinkedList<Order> orderList = new LinkedList<Order>();

	public void makeMoney(List<Price> lists) {
		for (Price price : lists) {
			String times = price.getTimes();
			BigDecimal currentPrice = price.getPrice();
			//
			if (orderList.size() == 0) {
				buyMinOrder(times, currentPrice);
				maxPrice = currentPrice;
				minPrice = currentPrice;
			} else if (Bdc.lt(currentPrice, minPrice, 2)) {
				if (buyMin(currentPrice)) {
					buyMinOrder(times, currentPrice);
				}

			} else if (Bdc.mt(currentPrice, maxPrice, 2)) {
				if (buyMax(currentPrice, maxPrice)) {
					buyMaxOrder(times, currentPrice);
				}
			} else if (orderList.size() >= 3) {//
				saleOrder(times, currentPrice);
			}
			latestPrice = price.getPrice();
		}
		log("totalAmt: " + totalAmt);
		for (Order item : orderList) {
			log("Hold...." + item.getTimes() + " # " + item.getQty() + " # " + item.getPrice() + " # " + item.getAmt().toPlainString());
			totalAmt = totalAmt.add(item.getCurAmt(latestPrice));
		}
		log("totalAmt: " + totalAmt);
		log("totalUsedAmt: " + initAmt.subtract(totalMinAmt).toPlainString());
		log("Rate: " + (totalAmt.subtract(initAmt)).divide(initAmt.subtract(totalMinAmt), BigDecimal.ROUND_HALF_UP));
	}

	//
	private void saleOrder(String times, BigDecimal currentPrice) {
		Order minOrder = orderList.element();
		if (cansale(currentPrice, minOrder)) {
			log("sale......." + times + " # " + currentPrice);
			//
			Order order = orderList.remove();
			totalAmt = totalAmt.add(order.getCurAmt(currentPrice));
			//
			order = orderList.remove();
			totalAmt = totalAmt.add(order.getCurAmt(currentPrice));
			//
			order = orderList.remove();
			totalAmt = totalAmt.add(order.getCurAmt(currentPrice));
			if (Bdc.mt(totalMinAmt, totalAmt, 2)) {
				totalMinAmt = totalAmt;
			}
		}
	}

	private boolean cansale(BigDecimal currentPrice, Order order) {
		BigDecimal risePercent = risePercent(currentPrice, order.getPrice());
		return Bdc.mt(risePercent, rate.multiply(layer), 2);
	}

	//
	private BigDecimal risePercent(BigDecimal currentPrice, BigDecimal price) {
		return currentPrice.subtract(price).divide(price, BigDecimal.ROUND_HALF_UP);
	}

	//
	private boolean buyMin(BigDecimal currentPrice) {
		return Bdc.lt(minPrice.subtract(currentPrice).divide(minPrice, BigDecimal.ROUND_HALF_UP), rate, 2);
	}

	//
	private boolean buyMax(BigDecimal currentPrice, BigDecimal maxPrice) {
		return Bdc.mt(currentPrice.subtract(maxPrice).divide(maxPrice, BigDecimal.ROUND_HALF_UP), rate, 2);
	}

	//
	private void buyMinOrder(String times, BigDecimal price) {
		Integer qty = getQtyByPrice(price);
		if (qty == 0) {
			return;
		}
		log("buy min...." + times + " # " + price);
		minPrice = price;
		orderList.addFirst(new Order(times, price, qty));
		totalAmt = totalAmt.subtract(price.multiply(new BigDecimal(qty)));

		if (Bdc.mt(totalMinAmt, totalAmt, 2)) {
			totalMinAmt = totalAmt;
		}
	}

	//
	private void buyMaxOrder(String times, BigDecimal price) {
		Integer qty = getQtyByPrice(price);
		if (qty == 0) {
			return;
		}
		log("buy max...." + times + " # " + price);
		//
		maxPrice = price;
		orderList.add(new Order(times, price, qty));
		totalAmt = totalAmt.subtract(price.multiply(new BigDecimal(qty)));
		//
		if (Bdc.mt(totalMinAmt, totalAmt, 2)) {
			totalMinAmt = totalAmt;
		}
	}

	//
	private Integer getQtyByPrice(BigDecimal price) {
		Integer qty = 0;
		if (price.floatValue() <= 9) {
			qty = 300;
		} else if (price.floatValue() > 9 && price.floatValue() <= 15.0) {
			qty = 200;
		} else if (price.floatValue() > 15 && price.floatValue() <= 30) {
			qty = 100;
		}
		return qty;
	}

	private void log(String s) {
		System.out.println(s);
	}
}
