package com.cn.proxy.server.ana.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author tben
 *
 */
public class CommonDateTimeUtils {
	private static final String DEFAULT_DAY_TIMEFORMAT_PATTERN = "yyyy-MM-dd";
	private static final String DEFAULT_DATETIME_TIMEFORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String US_DATETIME_TIMEFORMAT_PATTERN = "MM/dd/yyyy HH:mm:ss";

	/**
	 * 
	 * @return
	 */
	public static String getDate() {
		return getDate(DEFAULT_DAY_TIMEFORMAT_PATTERN);
	}

	/**
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getDate(String pattern) {
		return getTime(pattern, new Date());
	}

	/**
	 * Function:getDate<br/>
	 * 
	 * @author Tony Ben
	 * @param date
	 * @return
	 * @since JDK 1.6
	 */
	public static String getDate(Date date) {
		return getTime(DEFAULT_DAY_TIMEFORMAT_PATTERN, date);
	}

	public static Date getTimeToDate(String date) {
		return getTime(DEFAULT_DAY_TIMEFORMAT_PATTERN, date);
	}

	public static Date getTimeToDate(String date, String pattern) {
		return getTime(pattern, date);
	}

	/**
	 * Function:getDate<br/>
	 * 
	 * @author Tony Ben
	 * @param pattern
	 * @param date
	 * @return
	 * @since JDK 1.6
	 */
	public static String getDate(String pattern, Date date) {
		return getTime(pattern, date);
	}

	/**
	 * 
	 * @return
	 */
	public static String getDateTime() {
		return getDateTime(DEFAULT_DATETIME_TIMEFORMAT_PATTERN);
	}

	/**
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getDateTime(String pattern) {
		pattern = CommonStringUtils.isEmpty(pattern) ? DEFAULT_DATETIME_TIMEFORMAT_PATTERN
				: pattern;
		return getTime(pattern, new Date());
	}

	/**
	 * Function:getUSDateTime<br/>
	 * 
	 * @author Tony Ben
	 * @return
	 * @since JDK 1.6
	 */
	public static String getUSDateTime() {
		return getDateTime(US_DATETIME_TIMEFORMAT_PATTERN);
	}

	/**
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String getTime(String pattern, Date date) {
		SimpleDateFormat sFormat = new SimpleDateFormat(pattern);
		return sFormat.format(date);
	}

	public static Date getTime(String pattern, String date) {
		SimpleDateFormat sFormat = new SimpleDateFormat(pattern);
		try {
			return sFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Function:getDateOffSet<br/>
	 * 
	 * @author Tony Ben
	 * @param offset
	 * @return
	 * @since JDK 1.6
	 */
	public static Date getDateOffSet(int offset) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, offset);
		return cal.getTime();
	}
	
	/**
	 * Function:getDateByLongMS<br/> 
	 * @author Tony Ben 
	 * @param millis
	 * @return 
	 * @since JDK 1.6
	 */
	public static Date getDateByLongMS(long millis){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal.getTime();
	}
}
