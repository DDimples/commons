package com.share.commons.log.impl;

/**
 * 用于注入几个日志用的属性
 *
 */
public class LogBean {

	private String businessLine;
	private String appName;
	private String span;

	public String getBusinessLine() {
		return this.businessLine;
	}

	public void setBusinessLine(String businessLine) {
		LogHolder.setBusinessLine(businessLine);
		this.businessLine = businessLine;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		LogHolder.setAppName(appName);
		this.appName = appName;
	}

	public String getSpan() {
		return span;
	}

	public void setSpan(String span) {
		LogHolder.setSpan(span);
		this.span = span;
	}
}
