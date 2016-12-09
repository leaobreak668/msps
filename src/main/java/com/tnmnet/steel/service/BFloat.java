package com.tnmnet.steel.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class BFloat extends BigDecimal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BFloat(BigInteger arg0) {
		super(arg0);
	}

	public BFloat(BigInteger unscaledVal, int scale, MathContext mc) {
		super(unscaledVal, scale, mc);
		// TODO Auto-generated constructor stub
	}

	public BFloat(BigInteger unscaledVal, int scale) {
		super(unscaledVal, scale);
		// TODO Auto-generated constructor stub
	}

	public BFloat(BigInteger val, MathContext mc) {
		super(val, mc);
		// TODO Auto-generated constructor stub
	}

	public BFloat(char[] in, int offset, int len, MathContext mc) {
		super(in, offset, len, mc);
		// TODO Auto-generated constructor stub
	}

	public BFloat(char[] in, int offset, int len) {
		super(in, offset, len);
		// TODO Auto-generated constructor stub
	}

	public BFloat(char[] in, MathContext mc) {
		super(in, mc);
		// TODO Auto-generated constructor stub
	}

	public BFloat(char[] in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	public BFloat(double val, MathContext mc) {
		super(val, mc);
		// TODO Auto-generated constructor stub
	}

	public BFloat(double val) {
		super(val);
		// TODO Auto-generated constructor stub
	}

	public BFloat(int val, MathContext mc) {
		super(val, mc);
		// TODO Auto-generated constructor stub
	}

	public BFloat(int val) {
		super(val);
		// TODO Auto-generated constructor stub
	}

	public BFloat(long val, MathContext mc) {
		super(val, mc);
		// TODO Auto-generated constructor stub
	}

	public BFloat(long val) {
		super(val);
		// TODO Auto-generated constructor stub
	}

	public BFloat(String val, MathContext mc) {
		super(val, mc);
		// TODO Auto-generated constructor stub
	}

	public BFloat(String val) {
		super(val);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BigDecimal divide(BigDecimal divisor) {
		return super.divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
	}
}