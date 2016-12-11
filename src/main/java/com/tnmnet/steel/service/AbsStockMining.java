package com.tnmnet.steel.service;

import java.math.BigDecimal;

public class AbsStockMining extends AbsMiningCal {

	protected Double getQtyByPrice(BigDecimal price) {
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
}