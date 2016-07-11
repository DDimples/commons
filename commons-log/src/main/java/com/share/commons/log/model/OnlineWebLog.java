package com.share.commons.log.model;

import org.springframework.util.SerializationUtils;

import java.util.Date;

/**
 *
 * 
 */
public class OnlineWebLog extends AbstractLog {
	/**
	 * "OnlineWebLog".hashCode()
	 */
	private static final long serialVersionUID = 1424841635;
	private String clientIP;
	private String logerName;
	private String logLevel;
	private Date logTime;
	private String logMsg;
	private String logContent;
	private String stackMessage;
	private String threadID;

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getLogerName() {
		return logerName;
	}

	public void setLogerName(String logerName) {
		this.logerName = logerName;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
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

	public String getThreadID() {
		return threadID;
	}

	public void setThreadID(String threadID) {
		this.threadID = threadID;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// should not happen
			byte[] bs = SerializationUtils.serialize(this);
			return SerializationUtils.deserialize(bs);
		}
	}

	@Override
	public String toString() {
		return new StringBuilder("OnlineWebLog [productLine=")
				.append(getBusinessLine()).append(", appName=" + getAppName())
				.append(", serverName=").append(getServerName())
				.append(", serverIP=" + getServerIp()).append(", clientIP=")
				.append(clientIP).append(", methodName=" + getServiceName())
				.append(", logerName=").append(logerName)
				.append(", logLevel=" + logLevel).append(", logTime=")
				.append(logTime).append(", pageUrl=")
				.append(getPageUrl() + ", queryString=")
				.append(getQueryString()).append(", logMsg=")
				.append(logMsg + ", logContent=").append(logContent)
				.append(", stackMessage=" + stackMessage).append(", threadID=")
				.append(threadID).append("]").toString();
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

}
