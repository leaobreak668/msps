package com.tnmnet.steel;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.tnmnet.steel.entity.IOrder;
import com.tnmnet.steel.entity.OrderImpl;

public class MT {

	public static void main(String[] args) {
		LinkedList<OrderImpl> stockList = new LinkedList<OrderImpl>();
		float[] s = { 2f, 4f, 6.f, 7f, 8f };
		for (float f : s) {
			stockList.add(new OrderImpl("i", new BigDecimal(f), 1));
		}
		int index = 0;
		for (IOrder order : stockList) {
			if (order.getBuyPrice().intValue() > 5) {
				break;
			}
			index++;
		}
		stockList.add(index, new OrderImpl("i", new BigDecimal(5), 1));
		for (IOrder order : stockList) {
			System.out.println(order.getBuyPrice());
		}
	}

}
