package com.share.commons.log.impl;

import com.share.commons.util.StringUtil;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class LogPatternLayout extends PatternLayout {
	private String[] replcaeArray = null;
	private static final String REPLACE_STRING = "******";
	private String filterLabel;
	/**
	 * 增加对特殊字段的过滤，暂时只支持json格式日志
	 */
	@Override
	public String format(LoggingEvent event) {
		String logMessage = super.format(event);
		if(replcaeArray!=null && replcaeArray.length>0){
			if(StringUtil.isBlank(logMessage)){
				return logMessage;
			}
			for(String s:replcaeArray){
				logMessage = this.replace(logMessage, s);
			}
		}
		return logMessage;
	}

	public String replace(String message,String repStr){
		int index = StringUtil.indexOfIgnoreCase(message, "\""+repStr+"\":");
		if(index>-1){
			int num=0,endNum = 0,endNum1 =0, endNum2=0 ;
			num=StringUtil.indexOfIgnoreCase(message, "\""+repStr+"\":\"");
			if(num>-1){//说明value类型是字符串形式
				endNum1 = StringUtil.indexOfIgnoreCase(message,"\",", num);
				endNum2 = StringUtil.indexOfIgnoreCase(message,"\"}", num);
				if(endNum1>-1){
					endNum1+=1;
				}
				if(endNum2>-1){
					endNum2+=1;
				}
			}else{//说明value类型是数值形式
				num =index;
				endNum1 = StringUtil.indexOfIgnoreCase(message,",", num);
				endNum2 = StringUtil.indexOfIgnoreCase(message,"}", num);


			}
			if(endNum1>-1 && endNum2>-1){
				endNum = endNum2<endNum1?endNum2:endNum1;
			}else if(endNum1>-1){
				endNum=endNum1;
			}else{
				endNum=endNum2;
			}

			if(endNum>-1){
				message = StringUtil.replace(message,
						StringUtil.substring(message, num, endNum), REPLACE_STRING);
			}else{
				message = StringUtil.replace(message,
						StringUtil.substring(message, num), REPLACE_STRING);
			}
			return this.replace(message, repStr);
		}else{
			return message;
		}
	}

	public String getFilterLabel() {
		return filterLabel;
	}

	public void setFilterLabel(String filterLabel) {
		this.filterLabel = filterLabel;
		this.buildReplcaeArray(filterLabel);
	}

	private void buildReplcaeArray(String filterLabel){
		if(StringUtil.isNotBlank(filterLabel)){
			this.replcaeArray = StringUtil.split(filterLabel,",");
		}
	}
}
