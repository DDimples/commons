/**   
* @Title: TraceSnooperLog.java
* @Description: TODO(用一句话描述该文件做什么)
* @date 2015年1月29日 下午6:56:33 
* @version V1.0   
*/
package com.share.commons.log.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class TraceSnooperLog implements Serializable{

	private static final long serialVersionUID = 2145364068376818242L;
	@JSONField(name="MethodName")
	private String methodName;
	@JSONField(name="ElapsedTime")
	private String elapsedTime;
	@JSONField(name="LogTime")
	private String logTime;
	@JSONField(name="ThreadID")
	private String threadID;
	@JSONField(name="ResponseCode")
	private String responseCode;
	@JSONField(name="BusinessErrorCode")
	private String businessErrorCode;
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	public String getThreadID() {
		return threadID;
	}
	public void setThreadID(String threadID) {
		this.threadID = threadID;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getBusinessErrorCode() {
		return businessErrorCode;
	}
	public void setBusinessErrorCode(String businessErrorCode) {
		this.businessErrorCode = businessErrorCode;
	}
	@Override
	public String toString() {
		return "TraceSnooperLog [methodName=" + methodName + ", elapsedTime="
				+ elapsedTime + ", logTime=" + logTime + ", threadID="
				+ threadID + ", responseCode=" + responseCode
				+ ", businessErrorCode=" + businessErrorCode + "]";
	}
	
}
