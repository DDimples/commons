package com.share.commons.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public class DateUtil {
	private final static TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
	/**
	 * the pattern is "yyyy-MM-dd". for example:with a date(2014-08-17
	 * 09:01:07.238) the result will be 2014-08-17
	 */
	public static final String FORMAT_DEFAULT = "yyyy-MM-dd";
	/**
	 * the pattern is "yyyy-MM-dd HH:mm:ss". for example:with a date(2014-08-17
	 * 09:01:07.238) the result will be 2014-08-17 09:01:07
	 */
	public static final String FORMAT_ALL = "yyyy-MM-dd HH:mm:ss";
	/**
	 * the pattern is "yyyy-MM-dd HH:mm". for example:with a date(2014-08-17
	 * 09:01:07.238) the result will be 2014-08-17 09:01
	 */
	public static final String FORMAT_MIN = "yyyy-MM-dd HH:mm";
	/**
	 * the pattern is "yyyy年MM月dd日". for example:with a date(2014-08-17
	 * 09:01:07.238) the result will be 2014年08月17日
	 */
	public static final String FORMAT_CHINESE = "yyyy年MM月dd日";
	/**
	 * the pattern is "yyyy-MM-dd HH:mm:ss.SSS". for example:with a
	 * date(2014-08-17 09:01:07.238) the result will be 2014-08-17 09:01:07.238
	 */
	public static final String FORMAT_ALL_M = "yyyy-MM-dd HH:mm:ss.SSS";
	/**
	 * the pattern is "yyyyMMddHHmmss". for example:with a date(2014-08-17
	 * 09:01:07.238) the result will be 20140817090107
	 */
	public static final String FORMAT_SMALL = "yyyyMMddHHmmss";
	/**
	 * the pattern is "yyyyMMdd". for example:with a date(2014-08-17
	 * 09:01:07.238) the result will be 20140817
	 */
	public static final String FORMAT_DEFAULT_SMALL = "yyyyMMdd";
	/**
	 * the pattern is "HH:mm:ss". for example:with a date(2014-08-17
	 * 09:01:07.238) the result will be 09:01:07
	 */
	public static final String FORMAT_TIME = "HH:mm:ss";
	/**
	 * the pattern is "HH". for example:with a date(2014-08-17 09:01:07.238) the
	 * result will be 09
	 */
	public static final String FORMAT_TIME_HOUR = "HH";
	/**
	 * the pattern is "mm". for example:with a date(2014-08-17 09:01:07.238) the
	 * result will be 01
	 */
	public static final String FORMAT_TIME_MINUTE = "mm";
	/**
	 * the pattern is "ss". for example:with a date(2014-08-17 09:01:07.238) the
	 * result will be 07
	 */
	public static final String FORMAT_TIME_SECOND = "ss";
	/**
	 * the pattern is "MM月dd日". for example:with a date(2014-08-17 09:01:07.238)
	 * the result will be 08月17日
	 */
	public static final String FORMAT_CHINESE_MONTH_DAY = "MM月dd日";
	/**
	 * the pattern is "M月d日". for example:with a date(2014-08-17 09:01:07.238)
	 * the result will be 8月17日
	 */
	public static final String FORMAT_CHINESE_SIMPLE_MONTH_DAY = "M月d日";

	private static FastDateFormat getDateFormat(String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("pattern could not be null");
		}
		return FastDateFormat.getInstance(pattern);
	}

	public static int getIntervalDays(String startdate, String enddate) {
		try {
			FastDateFormat format = getDateFormat(FORMAT_DEFAULT);
			Date d1 = format.parse(startdate);
			Date d2 = format.parse(enddate);
			long ei = d2.getTime() - d1.getTime();
			int dd = (int) (ei / (1000 * 60 * 60 * 24));
			return dd;
		} catch (ParseException e) {
			// e.printStackTrace();
		}
		return 0;
	}

	/**
	 * make calendar instance for GMT+8
	 * 
	 * @return
	 */
	private static Calendar getInstance() {
		return GregorianCalendar.getInstance(timeZone);
	}

	/**
	 * 四舍五入
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static int getIntervalDays(Date startdate, Date enddate) {
		return (int) ((enddate.getTime() - startdate.getTime()) / (1000 * 60 * 60 * 24));
	}

	public static String getToday() {
		return getToday(FORMAT_ALL);
	}

	public static String getToday(String format) {
		return getDateFormat(format).format(new Date());
	}

	/**
	 * 以当前时间加天数的日期
	 * 
	 * @param day
	 *            推移的天数
	 * @return
	 */
	public static String addCurrentDate(int day) {
		Calendar calendar = getInstance();
		calendar.add(Calendar.DATE, day);
		return getDateFormat(FORMAT_DEFAULT).format(calendar.getTime());
	}

	/**
	 * 将一个字符串的日期描述转换为java.util.Date对象
	 *
	 * @param strDate
	 *            字符串的日期描述
	 * @param format
	 *            字符串的日期格式，比如:“yyyy-MM-dd HH:mm”
	 * @return 字符串转换的日期对象java.util.Date
	 */
	public static Date getDate(String strDate, String format) {
		if (StringUtil.isBlank(strDate)) {
			return null;
		}

		FastDateFormat formatter = getDateFormat(format);
		Date date = null;
		try {
			date = formatter.parse(strDate);
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
		}
		return date;
	}

	/**
	 * @param msel
	 *            毫秒时间
	 * @param format
	 *            日期格式
	 * @return 毫秒时间 所对应的日期
	 */

	public static String formatTime(long msel, String format) {
		Date date = new Date(msel);
		return getDateFormat(format).format(date);
	}

	/**
	 * 解析时间成字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return getDateFormat(FORMAT_ALL).format(date);
	}

	/**
	 * 格式化字符串日期
	 * 
	 * @param strDate
	 * @param strOldFormat
	 * @param strNewFormat
	 * @return
	 */
	public static String formatDate(String strDate, String strOldFormat,
			String strNewFormat) {
		if (strDate == null || strDate.trim().length() == 0) {
			return "";
		}
		if (strOldFormat == null || strOldFormat.trim().length() == 0) {
			strOldFormat = FORMAT_ALL;
		}
		if (strNewFormat == null || strNewFormat.trim().length() == 0) {
			strNewFormat = FORMAT_DEFAULT;
		}
		Date date = getDate(strDate, strOldFormat);
		return getDateFormat(strNewFormat).format(date);
	}

	/**
	 * 解析时间成指定字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		return getDateFormat(pattern).format(date);
	}

	/**
	 * 解析指定字符串为日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String source, String pattern)
			throws ParseException {
		if (StringUtil.isBlank(source) || StringUtil.isBlank(pattern)) {
			return null;
		}
		try {
			return FastDateFormat.getInstance(pattern).parse(source);
		} catch (ParseException e) {
			throw e;
		}
	}

	public static Date getCurrentTime() {
		return getInstance().getTime();
	}

	public static Date plusDays(Date time, int days) {
		Calendar cal = getInstance();
		cal.setTime(time);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	/**
	 * 距离小时数
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws Exception
	 */
	public static int getIntervalHour(String startdate, String enddate)
			throws Exception {
		FastDateFormat dateFormat = getDateFormat(FORMAT_ALL);
		Date bDate = dateFormat.parse(startdate);
		Date eDate = dateFormat.parse(enddate);

		long ei = eDate.getTime() - bDate.getTime();
		int dd = (int) (ei / (1000 * 60 * 60));
		return dd < 1 ? 1 : dd;
	}

	/**
	 * 转换字符串为long型毫秒
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long date2Long(String date) throws ParseException {
		Date bDate = getDateFormat(FORMAT_ALL).parse(date);
		return bDate.getTime();
	}

	/**
	 * long型毫秒转换为字符串
	 * 
	 * @param msel
	 * @param format
	 * @return
	 */
	public static String long2DateStr(long msel, String format) {
		return getDateFormat(format).format(new Date(msel));
	}
	
	public static long getCurrentMSEL() {
		return System.currentTimeMillis();
	}

	/**
	 * 格式化字符串日期
	 * 
	 * @param strDate
	 * @param strOldFormat
	 * @param strNewFormat
	 * @return
	 */
	public static String getDate(String strDate, String strOldFormat,
			String strNewFormat) {
		if (strDate == null || strDate.trim().length() == 0) {
			return "";
		}
		if (strOldFormat == null || strOldFormat.trim().length() == 0) {
			strOldFormat = FORMAT_ALL;
		}
		if (strNewFormat == null || strNewFormat.trim().length() == 0) {
			strNewFormat = FORMAT_DEFAULT;
		}
		Date date = getDate(strDate, strOldFormat);
		return getDateFormat(strNewFormat).format(date);
	}

	/**
	 * 日期格式化，自定义输出日期格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date, String dateFormat) {
		return getDateFormat(dateFormat).format(date);
	}

	/**
	 * 是否为当天
	 * 
	 * @param startDate
	 * @return
	 */
	public static boolean isCurrentDay(String startDate, String format) {
		if (startDate == null || startDate.trim().length() == 0) {
			return false;
		}
		Date start = null;
		try {
			start = getDateFormat(format).parse(startDate);
		} catch (ParseException e) {
			//
		}
		if (start == null) {
			return false;
		}
		return DateUtils.isSameDay(start, new Date());
	}

	public static int getWeekDayFromCalendar(Date date) {
		Calendar calendar = getInstance();
		calendar.setTime(date);
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			return 7;
		case Calendar.MONDAY:
			return 1;
		case Calendar.TUESDAY:
			return 2;
		case Calendar.WEDNESDAY:
			return 3;
		case Calendar.THURSDAY:
			return 4;
		case Calendar.FRIDAY:
			return 5;
		case Calendar.SATURDAY:
			return 6;
		default:
			return 0;
		}
	}

	public static int compareDate(Date beforeDate, Date afterDate) {
		return beforeDate == null ? (afterDate == null ? 0 : -1)
				: (afterDate == null ? 1 : beforeDate.compareTo(afterDate));
	}

	/**
	 * 传来的日期增加传来的天数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static String addDayByNum(String date, int day) {
		try {
			FastDateFormat dateFormat = getDateFormat(FORMAT_DEFAULT);
			Date dateTemp = dateFormat.parse(date);
			Calendar calendar = getInstance();
			calendar.setTime(dateTemp);
			calendar.add(Calendar.DATE, day);
			return dateFormat.format(calendar.getTime());
		} catch (ParseException e) {
			return date;
		}
	}

	/**
	 * 判断curdate是否在startDate和endDate之间
	 * 
	 * @param curdate
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isDateInRange(String curdate, String startDate,
			String endDate) {
		if (curdate == null || curdate.isEmpty() || startDate == null
				|| startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
			return false;
		}
		FastDateFormat dateFormat = getDateFormat(FORMAT_DEFAULT);
		try {
			Date sDate = addDay(startDate, -1, dateFormat);
			Date eDate = addDay(endDate, -1, dateFormat);

			Date curDateTmp = dateFormat.parse(curdate);
			return (sDate.before(curDateTmp)) && (curDateTmp.before(eDate));
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 传来的日期增加传来的天数--DAY_OF_YEAR
	 * 
	 * @param date
	 *            格式为{@link #FORMAT_DEFAULT}
	 * @param offset
	 * @return
	 * @see #FORMAT_DEFAULT
	 */
	public static String addDay(String date, int offset) {
		try {
			FastDateFormat dateFormat = getDateFormat(FORMAT_DEFAULT);
			return dateFormat.format(addDay(date, offset, dateFormat));
		} catch (Exception e) {
			return null;
		}
	}

	public static Date addDay(String date, int offset, String format) {
		return addDay(date, offset, getDateFormat(format));
	}

	private static Date addDay(String date, int offset,
			FastDateFormat dateFormat) {
		try {
			Date dateTemp = dateFormat.parse(date);
			Calendar calendar = getInstance();
			calendar.setTime(dateTemp);
			calendar.add(Calendar.DATE, offset);
			return calendar.getTime();
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 传来的日期增加传来的天数--DAY_OF_YEAR
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static String addDayOfYearWithTime(String date, int day) {
		FastDateFormat formatter = getDateFormat(FORMAT_ALL);
		try {
			Date dateTemp = formatter.parse(date);
			Calendar calendar = getInstance();
			calendar.setTime(dateTemp);
			calendar.add(Calendar.DAY_OF_YEAR, day);
			return formatter.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	/**
	 * 所给日期，是否早于系统日期之前
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static boolean isBefore(String dateStr) {
		if (dateStr == null || dateStr.trim().length() == 0) {
			return false;
		}

		Date date = getDate(dateStr, FORMAT_ALL);

		Calendar today = getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		if (date.compareTo(today.getTime()) <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 和系统时间相比，是否同一天
	 * 
	 * @param dateStr
	 *            "yyyy-MM-dd"
	 * @author haibo.tang 2012-2-28
	 */
	public static boolean isToday(String dateStr) {
		if (StringUtil.isBlank(dateStr)) {
			return false;
		}
		Date date = DateUtil.getDate(dateStr, FORMAT_DEFAULT);
		if (date == null) {
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return DateUtils.isSameDay(Calendar.getInstance(), calendar);
	}

	/**
	 * 和系统时间相比，是否昨天
	 * 
	 * @param dateStr
	 *            "yyyy-MM-dd"
	 * @author haibo.tang 2012-2-28
	 */
	public static boolean isYesterday(String dateStr) {
		if (StringUtil.isBlank(dateStr)) {
			return false;
		}
		Date date = getDate(dateStr, FORMAT_DEFAULT);
		if (date == null) {
			return false;
		}
		Calendar yest = getInstance();
		yest.add(Calendar.DAY_OF_MONTH, -1);
		Calendar compar = getInstance();
		compar.setTime(date);

		return yest.get(Calendar.YEAR) == compar.get(Calendar.YEAR)
				&& yest.get(Calendar.DAY_OF_YEAR) == compar
						.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取时间差值
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String getIntervalTime(String str1, String str2) {
		if (StringUtil.isBlank(str1) || StringUtil.isBlank(str2)) {
			return "";
		}
		try {
			FastDateFormat df = getDateFormat(FORMAT_MIN);
			if (df == null) {
				return "";
			}
			Date one = df.parse(str1);
			Date two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			long min = diff / (60 * 1000);
			long hour = min / 60;
			min = min % 60;
			long day = hour / 24;
			hour = hour % 24;
			StringBuffer result = new StringBuffer();
			if (day > 0) {
				result.append(day + "天");
			}
			if (hour > 0) {
				result.append(hour + "小时");
			}
			if (min > 0) {
				result.append(min + "分钟");
			}
			return result.toString();
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 转换字符串为long型毫秒
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long date2Long(String date, String formatStr)
			throws ParseException {
		FastDateFormat format = getDateFormat(formatStr);
		Date bDate = format.parse(date);
		return bDate.getTime();
	}

	/**
	 * 搜索日期小于当前日期，则返回true
	 * 
	 * @param startDate
	 * @return
	 */
	public static boolean invalidDate(String startDate) {
		FastDateFormat sdf = getDateFormat(FORMAT_DEFAULT);
		boolean valid = false;
		try {
			Date start = sdf.parse(startDate);
			valid = start.before(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return valid;
	}

	/**
	 * 所给时间，是否在系统时间之前
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static boolean isBeforeCurrent(String dateStr) throws Exception {
		if (dateStr == null || dateStr.trim().length() == 0) {
			return false;
		}
		Date date = DateUtil.getDate(dateStr, FORMAT_ALL);
		return date.getTime() < System.currentTimeMillis();
	}

	/**
	 * 订单列表使用，返回格式：8月29日
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {
		if (StringUtil.isNotBlank(date)) {
			try {
				return DateUtil.formatDate(date, FORMAT_DEFAULT,
						FORMAT_CHINESE_SIMPLE_MONTH_DAY);
			} catch (Exception e) {
			}
		}
		return date;
	}

	/**
	 * 获取间隔分钟
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws Exception
	 */
	public static int getIntervalMinutes(String startdate, String enddate)
			throws Exception {
		FastDateFormat dateFormat = getDateFormat(FORMAT_ALL);
		Date bDate = dateFormat.parse(startdate);
		Date eDate = dateFormat.parse(enddate);

		long ei = eDate.getTime() - bDate.getTime();
		int dd = (int) (ei / (1000 * 60));
		return dd < 1 ? 1 : dd;
	}

	/**
	 * 将以点号或者斜线等分割的日期字符串转为统一格式的date,对于不能转换的字符串，返回为空
	 * 
	 * @param date
	 * @param formart
	 * @return
	 * @throws ParseException
	 */
	public static Date safeConvert(String date, String formart)
			throws ParseException {

		if ("".equalsIgnoreCase(date) || date == null) {
			return null;
		}
		String temp = date.replace(".", "-").replace("/", "-")
				.replace(" ", "-");
		return parseDate(temp, FORMAT_DEFAULT);
	}

}
