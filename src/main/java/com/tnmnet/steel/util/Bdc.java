package com.tnmnet.steel.util;

import java.math.BigDecimal;

public class Bdc {

	public static int compare(BigDecimal bigDecimal, BigDecimal compareTo, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(compareTo.setScale(scale, BigDecimal.ROUND_HALF_UP));
	}

	public static boolean mt(BigDecimal bigDecimal, BigDecimal bigDecimal2, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(bigDecimal2.setScale(scale, BigDecimal.ROUND_HALF_UP)) > 0;
	}

	public static boolean me(BigDecimal bigDecimal, BigDecimal bigDecimal2, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(bigDecimal2.setScale(scale, BigDecimal.ROUND_HALF_UP)) >= 0;
	}

	/**
	 * Less than
	 * 
	 * @param bigDecimal
	 * @param scale
	 * @return
	 */
	public static boolean ltZero(BigDecimal bigDecimal, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("0").setScale(scale, BigDecimal.ROUND_HALF_UP)) < 0;
	}

	/**
	 * Less equals
	 * 
	 * @param bigDecimal
	 * @param scale
	 * @return
	 */
	public static boolean leZero(BigDecimal bigDecimal, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("0").setScale(scale, BigDecimal.ROUND_HALF_UP)) <= 0;
	}

	/**
	 * More than
	 * 
	 * @param bigDecimal
	 * @param scale
	 * @return
	 */
	public static boolean mtZero(BigDecimal bigDecimal, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("0").setScale(scale, BigDecimal.ROUND_HALF_UP)) > 0;
	}

	/**
	 * More equals
	 * 
	 * @param bigDecimal
	 * @param scale
	 * @return
	 */
	public static boolean meZero(BigDecimal bigDecimal, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("0").setScale(scale, BigDecimal.ROUND_HALF_UP)) >= 0;
	}

	/**
	 * Equals zero
	 * 
	 * @param bigDecimal
	 * @param scale
	 * @return
	 */
	public static boolean eqZero(BigDecimal bigDecimal, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("0").setScale(scale, BigDecimal.ROUND_HALF_UP)) == 0;
	}

	/**
	 * Not equal zero
	 * 
	 * @param bigDecimal
	 * @param scale
	 * @return
	 */
	public static boolean neZero(BigDecimal bigDecimal, int scale) {
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("0").setScale(scale, BigDecimal.ROUND_HALF_UP)) != 0;
	}

	/**
	 * 
	 * @param first
	 * @param second
	 * @param scale
	 * @return
	 */
	public static boolean ge(BigDecimal first, BigDecimal second, int scale) {
		return first.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(second.setScale(scale, BigDecimal.ROUND_HALF_UP)) >= 0;
	}

	/**
	 * 
	 * @param first
	 * @param second
	 * @param scale
	 * @return
	 */
	public static boolean gt(BigDecimal first, BigDecimal second, int scale) {
		return first.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(second.setScale(scale, BigDecimal.ROUND_HALF_UP)) > 0;
	}

	/**
	 * 
	 * @param first
	 * @param second
	 * @param scale
	 * @return
	 */
	public static boolean le(BigDecimal first, BigDecimal second, int scale) {
		return first.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(second.setScale(scale, BigDecimal.ROUND_HALF_UP)) <= 0;
	}

	/**
	 * 
	 * @param first
	 * @param second
	 * @param scale
	 * @return
	 */
	public static boolean lt(BigDecimal first, BigDecimal second, int scale) {
		return first.setScale(scale, BigDecimal.ROUND_HALF_UP).compareTo(second.setScale(scale, BigDecimal.ROUND_HALF_UP)) < 0;
	}
}