package com.share.commons.log.model;


import com.share.commons.log.impl.LogUtil;

/**
 * 大日志
 *
 */
public class HBaseLog extends Log {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9081551069894125782L;
	private String deviceId;
	private String clientIp;
	private String methodName;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	protected static final char[] NULL = "null".toCharArray();

	@Override
	public String toString() {
		final char[] NULL = HBaseLog.NULL;
		StringBuilder builder = new StringBuilder(1024);
		builder.append("1.0").append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getLogTime())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getTraceId())).append(TAB);
		builder.append(NULL).append(TAB);// span not the same one with LogHolder
		builder.append(LogUtil.replaceTabAndEnter(this.getBusinessLine())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getLogType())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getServerName())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getServerIp())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getUserLogType())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getSessionId())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getDeviceId())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getAppName())).append(TAB);
		String methodName = getMethodName();
		if (methodName == null) {
			methodName = getServiceName();
		}
		builder.append(LogUtil.replaceTabAndEnter(methodName)).append(TAB);//method or service
		builder.append(LogUtil.replaceTabAndEnter(this.getElapsedTime())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getResponseCode())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getBusinessErrorCode())).append(
				TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getExceptionMsg())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(LogUtil.getThrowableDetail(getException()),
						"#")).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getRequestHeader())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getRequestBody())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getResponseBody())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getPageUrl())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getClientIp())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getHadoopContent())).append(TAB);
		builder.append(LogUtil.replaceTabAndEnter(this.getExtend1()));
		return builder.toString();
	}

}
