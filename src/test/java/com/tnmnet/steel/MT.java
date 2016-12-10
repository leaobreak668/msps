package com.tnmnet.steel;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.tnmnet.steel.entity.Order;

public class MT {

	public static void main(String[] args) {
		LinkedList<Order> stockList = new LinkedList<Order>();
		float[] s = { 2f, 4f, 6.f, 7f, 8f };
		for (float f : s) {
			stockList.add(new Order("i", new BigDecimal(f), 1));
		}
		int index = 0;
		for (Order order : stockList) {
			if (order.getBuyPrice().intValue() > 5) {
				break;
			}
			index++;
		}
		stockList.add(index, new Order("i", new BigDecimal(5), 1));
		for (Order order : stockList) {
			System.out.println(order.getBuyPrice());
		}
	}

}
