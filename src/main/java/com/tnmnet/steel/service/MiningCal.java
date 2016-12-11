package com.tnmnet.steel.service;

import java.math.BigDecimal;
import java.util.Date;

import com.tnmnet.steel.util.Bdc;
import com.tnmnet.steel.util.DateUtils;

public abstract class MiningCal {

	protected int calDays(Date d1, Date d2) {
		return DateUtils.daysBetween(d1, d2);
	}

	protected boolean moreThan(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
		return Bdc.mt(bigDecimal, bigDecimal2, 2);
	}

	protected boolean lessThan(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
		return Bdc.lt(bigDecimal, bigDecimal2, 2);
	}

	protected boolean eqZero(BigDecimal bigDecimal) {
		return Bdc.eqZero(bigDecimal, 2);
	}

	protected void log(String s) {
		System.out.println(s);
	}
}
