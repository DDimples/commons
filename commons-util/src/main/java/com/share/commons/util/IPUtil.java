package com.share.commons.util;

import javax.servlet.http.HttpServletRequest;

/**
 * zcj modify this Class by 2014-12-31
 */
public class IPUtil {
	
	public static String getClientIP(HttpServletRequest request) {
		String ip = "";
		String ipStr = request.getHeader("x-forwarded-for");
		if (StringUtil.isNotEmpty(ipStr)) {
			String[] ips = ipStr.split(",");
			if (ips != null && ips.length > 0) {
				ip = (ips[0] != null) ? ips[0].trim() : "";
			}
		}
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	/** 
	 * ip地址转成整数. 
	 * @param ip 
	 * @return 
	 */  
	public static long ip2long(String ip) {  
	    String[] ips = ip.split("[.]");  
	    long num = 16777216L*Long.parseLong(ips[0]) + 
	    		       65536L*Long.parseLong(ips[1]) + 
	    		       256*Long.parseLong(ips[2]) + 
	    		       Long.parseLong(ips[3]);  
	    return num;  
	}  
	  
	/** 
	 * 整数转成ip地址. 
	 * @param ipLong 
	 * @return 
	 */  
	public static String long2ip(long ipLong) {  
	    long mask[] = {0x000000FF,0x0000FF00,0x00FF0000,0xFF000000};  
	    long num = 0;  
	    StringBuffer ipInfo = new StringBuffer();  
	    for(int i=0;i<4;i++){  
	        num = (ipLong & mask[i])>>(i*8);  
	        if(i>0) ipInfo.insert(0,".");  
	        ipInfo.insert(0,Long.toString(num,10));  
	    }  
	    return ipInfo.toString();  
	}
}
