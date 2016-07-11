package com.share.commons.log.impl;

import com.share.commons.util.DateUtil;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.regex.Pattern;

public class LogUtil {

	private static final Logger applicationLog = new LogWrapper(
			"ApplicationLogger");
	private static final Logger commonLog = new LogWrapper("CommonLogger");
	private static final Logger monitorLogger = new LogWrapper("MonitorLogger");
	private static final Logger mQLogger = new LogWrapper("MQLogger");
	private static final Logger seoLogger = new LogWrapper("SEOLogger");
	private static final Logger stjLogger = new LogWrapper("STJLogger");

	public static Logger getStjlogger() {
		return stjLogger;
	}

	public static Logger getMonitorLogger() {
		return monitorLogger;
	}

	public static Logger getmQLogger() {
		return mQLogger;
	}

	public static Logger getApplicationLogger() {
		return applicationLog;
	}

	public static Logger getCommonLogger() {
		return commonLog;
	}

	public static Logger getSeologger() {
		return seoLogger;
	}

	/**
	 * 
	 * @param event
	 * @return null while event or throwable is null
	 */
	public static StringBuffer getMessageFromEvent(LoggingEvent event) {
		return getThrowableDetail(getThrowableFromEvent(event));
	}

	public static Throwable getThrowableFromEvent(LoggingEvent event) {
		if (event != null) {
			ThrowableInformation throwableInformation = event
					.getThrowableInformation();
			if (throwableInformation != null) {
				Throwable t = throwableInformation.getThrowable();
				return t;
			}
		}
		return null;
	}

	public static StringBuffer getThrowableDetail(Throwable t) {
		if (t != null) {
			StringWriter writer = new StringWriter(1024);
			PrintWriter s = new PrintWriter(writer);
			t.printStackTrace(s);
			return writer.getBuffer();
		}
		return null;
	}

	public static final Pattern rntPattern = Pattern.compile("[\r\n\t]");

	public static String replaceTabAndEnter(CharSequence cs) {
		return replaceTabAndEnter(cs, " ");
	}

	public static String replaceTabAndEnter(CharSequence cs, String replacement) {
		if (cs == null) {
			return null;
		}
		if (cs.length() <= 0) {
			return "";
		}
		return rntPattern.matcher(cs).replaceAll(replacement);
	}

	static void prt(Throwable t) {
		prt(LogUtil.getThrowableDetail(t));
	}

	static void prt(CharSequence msg) {
		System.out.println(DateUtil.formatDate(new Date()) + "["
				+ Thread.currentThread() + "]:" + msg);
	}
}
