package com.share.commons.log.trace;

public enum TracerEnum {


	CLIENT_RPC_SEND(1),
	CLIENT_RPC_RECEIVED(2),
	SERVER_RPC_RECEIVED(3),
	SERVER_RPC_RETURN(4);
	
	private TracerEnum(int status){
		this.status=status;
	}
	private int status;
	public int status() {
		return status;
	}

}

