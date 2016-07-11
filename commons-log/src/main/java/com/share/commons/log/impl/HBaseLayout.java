package com.share.commons.log.impl;

import com.share.commons.util.DateUtil;
import com.share.commons.util.OSUtil;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Date;

public class HBaseLayout extends Layout {

	private static final String logVersion = "1.0";
	private static final char SPAN = '\t';
	private static final char ENTER = '\n';
	private static final String NULL = "null";

	@Override
	public void activateOptions() {
		// do nothing
	}

	@Override
	public String format(LoggingEvent event) {
		StringBuilder sbd = create(logVersion);// logversion
		append(sbd, DateUtil.formatDate(new Date(), DateUtil.FORMAT_ALL_M));// time
		append(sbd, LogHolder.getTraceId());// traceid
		appendNull(sbd);// span deep of stack
		append(sbd, LogHolder.getBusinessLine());// productLine
		append(sbd, "0001");// logType
		append(sbd, OSUtil.getLocalName());// serverName
		append(sbd, OSUtil.getLocalIP());// serverIp
		appendNull(sbd);// userLogType
		append(sbd, LogHolder.getSessionId());// sessionId
		appendNull(sbd);// deviceId
		append(sbd, LogHolder.getAppName());// appName
		append(sbd, LogHolder.getActionName());// methodName
		append(sbd, 0);// elapsedTime
		append(sbd, 0);// responseCode
		append(sbd, 0);// businessErrorCode
		append(sbd, String.valueOf(event.getMessage()));// logContent
		append(sbd, LogUtil.getMessageFromEvent(event));// exception
		appendNull(sbd);// requestHeader
		appendNull(sbd);// requestBody
		appendNull(sbd);// responseBody
		append(sbd, LogHolder.getUrl());// fromUrl
		append(sbd, LogHolder.getClientIp());// clientIp
		appendNull(sbd);// hadoopContent
		appendNull(sbd);// extend1
		close(sbd);
		return sbd.toString();
	}

	private static StringBuilder create(String str) {
		return new StringBuilder(512).append(LogUtil.replaceTabAndEnter(str));
	}

	private static StringBuilder append(StringBuilder sbd, int num) {
		return append(sbd, String.valueOf(num));
	}

	private static StringBuilder append(StringBuilder sbd, CharSequence str) {
		if (str == null) {
			return appendNull(sbd);
		}
		return sbd.append(SPAN).append(LogUtil.replaceTabAndEnter(str));
	}

	private static StringBuilder appendNull(StringBuilder sbd) {
		return sbd.append(SPAN).append(NULL);
	}

	private static StringBuilder close(StringBuilder sbd) {
		return sbd.append(ENTER);
	}

	/**
	 * make appender ignore throwable by return false
	 */
	@Override
	public boolean ignoresThrowable() {
		return false;
	}

}
