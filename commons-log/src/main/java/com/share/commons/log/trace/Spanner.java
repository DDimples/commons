package com.share.commons.log.trace;


public class Spanner {

	private String traceId;

	private SpanTypeEnum spanType;

	private Spanner parentSpan;

	private boolean isTopSpan;

	private String rpcId;

	private String location;

	public Spanner(String traceId,String rpcId,String location,SpanTypeEnum spanType,boolean isTopSpan,Spanner parentSpan){
		this.traceId=traceId;
		this.spanType=spanType;
		this.parentSpan = parentSpan;
		this.isTopSpan=isTopSpan;
		this.rpcId=rpcId;
		this.location=location;
	}


	public String getTraceId() {
		return traceId;
	}


	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}


	public SpanTypeEnum getSpanType() {
		return spanType;
	}


	public void setSpanType(SpanTypeEnum spanType) {
		this.spanType = spanType;
	}


	public Spanner getParentSpan() {
		return parentSpan;
	}


	public void setParentSpan(Spanner parentSpan) {
		this.parentSpan = parentSpan;
	}


	public boolean isTopSpan() {
		return isTopSpan;
	}


	public void setTopSpan(boolean isTopSpan) {
		this.isTopSpan = isTopSpan;
	}


	public String getRpcId() {
		return rpcId;
	}


	public void setRpcId(String rpcId) {
		this.rpcId = rpcId;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}

	public String toSpanString(){
		return new StringBuilder().append(this.spanType.type()).append("|").append(this.location).toString();
	}


}
