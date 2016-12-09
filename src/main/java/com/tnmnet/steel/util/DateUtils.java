package com.tnmnet.steel.util;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * 返回这一天的最早的时候
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEarliest(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setCalendarTime(cal, 0, 0, 0, 0);
		return cal.getTime();
	}

	/**
	 * 返回这一天的最晚时候
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastest(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setCalendarTime(cal, 23, 59, 59, 999);
		return cal.getTime();
	}

	private static void setCalendarTime(Calendar cal, int hourOfDay, int minute, int second, int milliSecond) {
		cal.set(HOUR_OF_DAY, hourOfDay);
		cal.set(MINUTE, minute);
		cal.set(SECOND, second);
		cal.set(MILLISECOND, milliSecond);
	}

	public static String getDateString(Date date, String format) {
		if (date == null) {
			return "";
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Date formatDate(Date date, String format) {
		if (date == null) {
			return null;
		}
		try {
			String dateString = getDateString(date, format);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date afterDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, days);
		return cal.getTime();
	}

	public static Date afterMins(Date date, int mins) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, mins);
		return cal.getTime();
	}

	public static Date afterMonths(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	public static int daysBetween(Date early, Date late) {
		java.util.Calendar calst = java.util.Calendar.getInstance();
		java.util.Calendar caled = java.util.Calendar.getInstance();
		calst.setTime(early);
		caled.setTime(late);
		// 设置时间为0时
		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calst.set(java.util.Calendar.MINUTE, 0);
		calst.set(java.util.Calendar.SECOND, 0);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;

		return days;
	}

	public static int monthsBetween(Date early, Date lately) {
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(early);
		c2.setTime(lately);
		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		return result == 0 ? 1 : Math.abs(result);
	}

	public static boolean isLessThan(Date firstDate, Date seconndDate) {
		if (firstDate == null || seconndDate == null) {
			return false;
		}
		return firstDate.compareTo(seconndDate) < 0;
	}

	public static boolean isLessEquals(Date firstDate, Date seconndDate) {
		if (firstDate == null || seconndDate == null) {
			return false;
		}
		return firstDate.compareTo(seconndDate) <= 0;
	}

	public static boolean isGreaterEquals(Date firstDate, Date seconndDate) {
		if (firstDate == null || seconndDate == null) {
			return false;
		}
		return firstDate.compareTo(seconndDate) >= 0;
	}

	public static boolean isEquals(Date firstDate, Date seconndDate) {
		if (firstDate == null || seconndDate == null) {
			return false;
		}
		return firstDate.compareTo(seconndDate) == 0;
	}

	public static boolean isGreaterThan(Date firstDate, Date seconndDate) {
		if (firstDate == null || seconndDate == null) {
			return false;
		}
		return firstDate.compareTo(seconndDate) > 0;
	}

	public static String getYesterdayString(String format) {
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_YEAR, -1);
		return getDateString(today.getTime(), format);
	}

	public static Integer getLastWeekNumber() {
		Calendar today = Calendar.getInstance();
		today.add(Calendar.WEEK_OF_YEAR, -1);
		return Integer.parseInt(getWeekString(today.getTime()));
	}

	public static Integer getWeekNumber(Date date) {
		return Integer.parseInt(getWeekString(date));
	}

	public static String getWeekString(Date date) {
		return getDateString(date, "ww");
	}

	public static String getLastWeekLastDay(String format) {
		Calendar today = Calendar.getInstance();
		today.add(Calendar.WEEK_OF_YEAR, -1);
		today.set(Calendar.DAY_OF_WEEK, today.getActualMaximum(Calendar.DAY_OF_WEEK));
		today.add(Calendar.DAY_OF_YEAR, 1);
		return getDateString(today.getTime(), format);
	}

	public static String getLastMonthLastDay(String format) {
		Calendar today = Calendar.getInstance();
		today.add(Calendar.MONTH, -1);
		today.set(Calendar.DAY_OF_MONTH, today.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDateString(today.getTime(), format);
	}

	public static Integer getLastMonthNumber() {
		Calendar today = Calendar.getInstance();
		today.add(Calendar.MONTH, -1);
		return getMonthNumber(today.getTime());
	}

	public static Integer getMonthNumber(Date date) {
		return Integer.parseInt(getMonthString(date));
	}

	public static String getMonthString(Date date) {
		return getDateString(date, "yyyyMM");
	}

	public static String getTodayString(String format) {
		return getDateString(Calendar.getInstance().getTime(), format);
	}

	public static int dateCompare(Date date1, Date date2, String format) {
		String dateString1 = DateUtils.getDateString(date1, format);
		String dateString2 = DateUtils.getDateString(date2, format);
		//
		if (Long.parseLong(dateString1) > Long.parseLong(dateString2)) {
			return 1;
		} else if (Long.parseLong(dateString1) < Long.parseLong(dateString2)) {
			return -1;
		} else {
			return 0;
		}
	}

	public static Date initBeginOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date initEndOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static String getYearStr(Date date) {
		return getDateString(date, "yyyy");
	}

	public static int getYearNum(Date date) {
		return Integer.parseInt(getYearStr(date));
	}

	public static String getMonStr(Date date) {
		return getDateString(date, "MM");
	}

	public static int getMonNum(Date date) {
		return Integer.parseInt(getMonStr(date));
	}

	public static int yearsBetween(Date early, Date late) {
		if (early == null || late == null) {
			return 0;
		}
		early = formatDate(early, "yyyy-MM-dd");
		late = formatDate(late, "yyyy-MM-dd");
		return DateUtils.getYearNum(late) - DateUtils.getYearNum(early);
	}
}
