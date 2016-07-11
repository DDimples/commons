/**   
* @Title: TraceDataLog.java
* @Description: TODO(用一句话描述该文件做什么)
* @date 2015年1月29日 下午7:14:15 
* @version V1.0   
*/
package com.share.commons.log.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class TraceDataLog implements Serializable{

	private static final long serialVersionUID = -7237685563019986050L;
	@JSONField(name="MethodName")
	private String methodName;
	@JSONField(name="PageUrl")
	private String pageUrl;
	@JSONField(name="QueryString")
	private String queryString;
	@JSONField(name="LogLevel")
	private String logLevel;
	@JSONField(name="ContextThreadID")
	private String contextThreadID;
	@JSONField(name="InputParams")
	private String inputParams;
	@JSONField(name="ElapsedTime")
	private String elapsedTime;
	@JSONField(name="ExceptionMsg")
	private String exceptionMsg;
	@JSONField(name="DebugData")	
	private String debugData;
	@JSONField(name="DisplayJsonStr")	
	private String displayJsonStr;
	@JSONField(name="Msg")
	private String msg;
	@JSONField(name="LogContent")
	private String logContent;
	@JSONField(name="StackMessage")	
	private String stackMessage;
	@JSONField(name="ServerName")	
	private String serverName;
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getContextThreadID() {
		return contextThreadID;
	}
	public void setContextThreadID(String contextThreadID) {
		this.contextThreadID = contextThreadID;
	}
	public String getInputParams() {
		return inputParams;
	}
	public void setInputParams(String inputParams) {
		this.inputParams = inputParams;
	}
	public String getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	public String getDebugData() {
		return debugData;
	}
	public void setDebugData(String debugData) {
		this.debugData = debugData;
	}
	public String getDisplayJsonStr() {
		return displayJsonStr;
	}
	public void setDisplayJsonStr(String displayJsonStr) {
		this.displayJsonStr = displayJsonStr;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	public String getStackMessage() {
		return stackMessage;
	}
	public void setStackMessage(String stackMessage) {
		this.stackMessage = stackMessage;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	@Override
	public String toString() {
		return "TraceDataLog [methodName=" + methodName + ", pageUrl="
				+ pageUrl + ", queryString=" + queryString + ", logLevel="
				+ logLevel + ", contextThreadID=" + contextThreadID
				+ ", inputParams=" + inputParams + ", elapsedTime="
				+ elapsedTime + ", exceptionMsg=" + exceptionMsg
				+ ", debugData=" + debugData + ", displayJsonStr="
				+ displayJsonStr + ", msg=" + msg + ", logContent="
				+ logContent + ", stackMessage=" + stackMessage
				+ ", serverName=" + serverName + "]";
	}
	
}
