/**   
* @Title: SpanTypeEnum.java
* @Description: TODO(用一句话描述该文件做什么)
* @date 2015年1月7日 下午4:12:07 
* @version V1.0   
*/
package com.share.commons.log.trace;

public enum SpanTypeEnum {
	RPC_CLIENT_SEND("cs"),
	RPC_CLIENT_RECEIVED("cr"),
	RPC_SERVER_RECEIVED("sr"),
	RPC_SERVER_SEND("ss")

	;
	private SpanTypeEnum(String type){
		this.type=type;
	}

	private String type;

	public String type() {
		return type;
	}

}
