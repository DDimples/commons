/**   
* @Title: LogLoaderFacttory.java
* @Description: TODO(log模型实例化工厂类)
* @date 2014年11月26日 下午5:43:41 
* @version V1.0   
*/
package com.share.commons.log.impl;

import com.alibaba.fastjson.JSON;
import com.share.commons.util.DateUtil;
import com.share.commons.util.OSUtil;
import com.share.commons.log.model.*;
import org.apache.log4j.Level;

import java.text.MessageFormat;
import java.util.Date;

public class LogLoaderFactory  {
	
    public static final String FORMAT_ALL_M = "yyyy-MM-dd HH:mm:ss SSS";
    
	public static ApplicationLog newActionLog(){
		ApplicationLog log = new ApplicationLog();	
		log.setServiceName(LogHolder.getActionName());//调用服务接口的名称（可以使地址，action名称）
		log.setTraceId(LogHolder.getTraceId());
		log.setServerName(OSUtil.getLocalName());//服务器名
		log.setServerIp(OSUtil.getLocalIP());//服务器IP
		log.setLogTime(DateUtil.formatDate(new Date(), FORMAT_ALL_M));
		log.setBusinessLine(LogHolder.getBusinessLine());
		log.setAppName(LogHolder.getAppName());
		log.setLogType("0010");
		log.setSpan(LogHolder.getSpan());
		
		return log;
	}
	
	public static ApplicationLog newActionLog(String methodName){
		ApplicationLog elongLog = newActionLog();
		elongLog.setServiceName(methodName);
		return elongLog;
	}

	public static CheckListLog newCheckListLog() {
		CheckListLog log = new CheckListLog();
		log.setLogType("0010");
		fillLog(log);
		return log;
	}

	private static void fillLog(Log log) {
		log.setServiceName(LogHolder.getActionName());// 调用服务接口的名称（可以使地址，action名称）
		log.setTraceId(LogHolder.getTraceId());
		log.setServerName(OSUtil.getLocalName());// 服务器名
		log.setServerIp(OSUtil.getLocalIP());// 服务器IP
		log.setLogTime(DateUtil.formatDate(new Date(), FORMAT_ALL_M));
		log.setBusinessLine(LogHolder.getBusinessLine());
		log.setAppName(LogHolder.getAppName());
		log.setSpan(LogHolder.getSpan());
		log.setPageUrl(LogHolder.getUrl());
		log.setQueryString(LogHolder.getQueryString());
	}

	public static HBaseLog newHBaseLog() {
		HBaseLog hBaseLog = new HBaseLog();
		fillLog(hBaseLog);
		hBaseLog.setLogType("0001");
		return hBaseLog;
	}

	public static CheckListLog newCheckListLog(String methodName,String errorCode
			,String errorMsg){
		CheckListLog elongLog = newCheckListLog();
		elongLog.setServiceName(methodName);
		elongLog.setBusinessErrorCode(errorCode);
		elongLog.setExceptionMsg(errorMsg);
		return elongLog;
	}
	public static CheckListLog newCheckListLog(String methodName,String errorCode){
		CheckListLog elongLog = newCheckListLog();
		elongLog.setServiceName(methodName);
		elongLog.setBusinessErrorCode(errorCode);
		return elongLog;
	}
	public static CheckListLog newCheckListLog(String methodName,Throwable ex){
		CheckListLog elongLog = newCheckListLog();
		elongLog.setServiceName(methodName);
		elongLog.setException(ex);
		elongLog.setResponseCode(LogStatus.error.toStr());
		return elongLog;
	}
	public static CheckListLog newCheckListLog(String methodName,long totalTime){
		CheckListLog elongLog = newCheckListLog();
		elongLog.setServiceName(methodName);
		elongLog.setElapsedTime(totalTime+"");
		elongLog.setResponseCode(LogStatus.success.toStr());
		return elongLog;		
	}
	public static CheckListLog newCheckListLog(long totalTime,String errorCode,String errorMsg,
			Exception e){
		CheckListLog elongLog = newCheckListLog();
		elongLog.setException(e);
		elongLog.setBusinessErrorCode(errorCode);
		elongLog.setExceptionMsg(errorMsg);
		elongLog.setElapsedTime(totalTime+"");		
		return elongLog;
	}
	public static CheckListLog newCheckListLog(long totalTime,Exception ex){
		CheckListLog elongLog = newCheckListLog();
		elongLog.setException(ex);
		elongLog.setElapsedTime(totalTime+"");		
		return elongLog;
	}
	
	public static  CommonLog newCommonLog(){
		CommonLog log = new CommonLog();	
		log.setServiceName(LogHolder.getActionName());//调用服务接口的名称（可以使地址，action名称）
		log.setTraceId(LogHolder.getTraceId());
		log.setServerName(OSUtil.getLocalName());//服务器名
		log.setServerIp(OSUtil.getLocalIP());//服务器IP
		log.setLogTime(DateUtil.formatDate(new Date(), FORMAT_ALL_M));
		log.setBusinessLine(LogHolder.getBusinessLine());
		log.setAppName(LogHolder.getAppName());
		log.setLogType("0010");	
		return log;
	}
	
	
	public static CommonLog newCommonLog(String methodName){
		CommonLog elongLog = newCommonLog();
		elongLog.setServiceName(methodName);
		return elongLog;
	}
	
	public static SEOLog newSEOLog(long totalTime,Exception ex){
		SEOLog elongLog = newSEOLog();
		elongLog.setException(ex);
		elongLog.setElapsedTime(totalTime+"");		
		return elongLog;
	}
	
	public static  SEOLog newSEOLog(){
		SEOLog log = new SEOLog();	
		log.setServiceName(LogHolder.getActionName());//调用服务接口的名称（可以使地址，action名称）
		log.setTraceId(LogHolder.getTraceId());
		log.setServerName(OSUtil.getLocalName());//服务器名
		log.setServerIp(OSUtil.getLocalIP());//服务器IP
		log.setLogTime(DateUtil.formatDate(new Date(), FORMAT_ALL_M));
		log.setBusinessLine(LogHolder.getBusinessLine());
		log.setAppName(LogHolder.getAppName());
		log.setLogType("010");
		log.setSpan(LogHolder.getSpan());
		return log;
		
	}
	public static OnlineWebLog newOnlineWebLog(){
		OnlineWebLog log = new OnlineWebLog();	
		log.setServiceName(LogHolder.getActionName());//调用服务接口的名称（可以使地址，action名称）
		log.setTraceId(LogHolder.getTraceId());
		log.setServerName(OSUtil.getLocalName());//服务器名
		log.setServerIp(OSUtil.getLocalIP());//服务器IP
		log.setBusinessLine(LogHolder.getBusinessLine());
		log.setAppName(LogHolder.getAppName());
		log.setClientIP(LogHolder.getClientIp()); //客户端ip
		
		return log;
	}
	
	public static OnlineWebLog newOnlineWebLog(String methodName){
		OnlineWebLog elongLog = newOnlineWebLog();
		elongLog.setServiceName(methodName);
		return elongLog;
	}

	public static TraceDataLog newTraceDataLog(String title, Object objectInfo){
		TraceDataLog log = new TraceDataLog();	
		log.setContextThreadID(
				MessageFormat.format("{0}_{1}", 
				Thread.currentThread().getId(),System.currentTimeMillis()));	
		log.setMethodName(title);
		log.setExceptionMsg(JSON.toJSONString(objectInfo!=null? objectInfo:""));;
		return log;
	}
	public static TraceErrorLog newTraceErrorLog(String methodName, String stackMsg, Exception e){
		TraceErrorLog elongLog = new TraceErrorLog();
		elongLog.setMethodName(methodName);
		elongLog.setLogLevel(Level.ERROR.toString());
		elongLog.setLogTime(new Date());
		elongLog.setMsg(e.getMessage());
		elongLog.setServerName(LogHolder.getAppName());
        if (e.getMessage() != null && e.getMessage().toString().length()> 200)
        {
        		elongLog.setExceptionMsg(e.getMessage());
        }
        elongLog.setLogContent(elongLog.getLogContent()+e.toString());
        elongLog.setStackMessage(stackMsg);
		elongLog.setPageUrl(LogHolder.getUrl());
		elongLog.setQueryString(LogHolder.getQueryString());
		elongLog.setContextThreadID(
				MessageFormat.format("{0}_{1}", 
						Thread.currentThread().getId(),System.currentTimeMillis()));
		return elongLog;
	}
	public static TraceSnooperLog newTraceSnooperLog(String methodName,long totalTime){
		TraceSnooperLog elongLog = new TraceSnooperLog();
		elongLog.setMethodName(methodName);
		elongLog.setLogTime(DateUtil.formatDate(new Date(), FORMAT_ALL_M));
		elongLog.setBusinessErrorCode("");
		elongLog.setElapsedTime(totalTime+"");
		elongLog.setResponseCode(LogStatus.success.toStr());
		elongLog.setThreadID(Thread.currentThread().getId()+"");
		return elongLog;
	}
}
