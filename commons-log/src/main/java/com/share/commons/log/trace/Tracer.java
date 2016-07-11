/**   
* @Title: Tracer.java
* @Description: TODO(分布式追踪组件)
* @date 2015年1月7日 下午4:13:03 
* @version V1.0   
*/
package com.share.commons.log.trace;

import java.util.concurrent.atomic.AtomicInteger;

public class Tracer {

	private static final String ROOT_SPAN = "1";
	private final ThreadLocal<Spanner> spanThreadLocal = new ThreadLocal<Spanner>();
	private final AtomicInteger childRpcIdx = new AtomicInteger(0);
	private final AtomicInteger childSpanx = new AtomicInteger(0);
	private static final Tracer tracer = new Tracer();

	private Tracer() {
	}

	public static Tracer getTracer() {
		return tracer;
	}

	public Spanner startRootTrace(String traceHead,SpanTypeEnum spanTypeEnum) {
		String traceId = traceHead+"-"+NotifyId.getInstance().getUniqIDHashString();
		return startTrace(traceId, traceId + "-" + ROOT_SPAN, ROOT_SPAN,
				spanTypeEnum, true);

	}

	public Spanner startRootTrace(SpanTypeEnum spanTypeEnum) {
		String traceId = NotifyId.getInstance().getUniqIDHashString();
		return startTrace(traceId, traceId + "-" + ROOT_SPAN, ROOT_SPAN,
				spanTypeEnum, true);

	}

	public Spanner startRootTrace(String traceId, String rpcId,
			String location, SpanTypeEnum spanTypeEnum) {
		return startTrace(traceId, rpcId, location, spanTypeEnum, true);
	}

	public Spanner startTrace(SpanTypeEnum spanTypeEnum) {
		Spanner span = spanThreadLocal.get();
		if (span == null) {
			return null;
		}
		span = startTrace(null, null, null, spanTypeEnum, false);
		return span;
	}

	public Spanner startRpcTrace() {
		Spanner span = spanThreadLocal.get();
		if (span == null) {
			return null;
		}
		span = startTrace(null, null, null, SpanTypeEnum.RPC_CLIENT_SEND, false);
		return span;
	}

	public Spanner startTrace(String traceId, String rpcId, String location,
			SpanTypeEnum spantype, boolean isTopSpan) {

		Spanner span = spanThreadLocal.get();
		if (span == null && isTopSpan == true) {
			Spanner rootSpan = new Spanner(traceId, rpcId, location, spantype,
					isTopSpan, null);

			spanThreadLocal.set(rootSpan);
			span = rootSpan;
		} else {
			span = createChildSpan(span, spantype, isTopSpan);
		}

		return span;

	}

	public Spanner startRpcTrace(String traceId, String rpcId, String location,
			SpanTypeEnum spantype, boolean isTopSpan) {

		Spanner span = spanThreadLocal.get();
		if (span == null && isTopSpan == true) {
			Spanner rootSpan = new Spanner(traceId, rpcId, location, spantype,
					isTopSpan, null);
			span = rootSpan;
			spanThreadLocal.set(rootSpan);
		} else {
			span = createChildSpan(span, spantype, isTopSpan);
		}
		return span;
	}

	public Spanner endTrace() {
		Spanner span = spanThreadLocal.get();
		if (span != null) {
			if (!span.isTopSpan()) {
				spanThreadLocal.set(span.getParentSpan());
			} else {
				spanThreadLocal.set(span);
			}
			span = spanThreadLocal.get();
		} else {
			throw new RuntimeException("no span");
		}
		return span;
	}

	public Spanner endRpcTrace() {
		Spanner span = spanThreadLocal.get();
		if (span != null) {
			if (!span.isTopSpan()) {
				spanThreadLocal.set(span.getParentSpan());
			} else {
				spanThreadLocal.set(span);
			}

			switch (span.getSpanType()) {
			case RPC_CLIENT_SEND:
				span.setSpanType(SpanTypeEnum.RPC_CLIENT_RECEIVED);
				break;
			case RPC_SERVER_RECEIVED:
				span.setSpanType(SpanTypeEnum.RPC_SERVER_SEND);
			default:
				break;
			}
			return span;

		} else {
			throw new RuntimeException("no rpc span");
		}
	}

	public Spanner endRootTrace() {
		Spanner span = spanThreadLocal.get();
		if (span != null) {
			spanThreadLocal.set(null);
			switch (span.getSpanType()) {
			case RPC_CLIENT_SEND:
				span.setSpanType(SpanTypeEnum.RPC_CLIENT_RECEIVED);
				break;
			case RPC_SERVER_RECEIVED:
				span.setSpanType(SpanTypeEnum.RPC_SERVER_SEND);
			default:
				break;
			}
		}

		return span;
	}

	public void setRootSpan(Spanner rootSpan) {

		spanThreadLocal.set(rootSpan);
	}

	public Spanner getCurrentSpan() {
		return spanThreadLocal.get();
	}

	private Spanner createChildSpan(Spanner parentSpan,
			SpanTypeEnum spanTypeEnum, boolean isTopSpan) {

		String newSpan = new StringBuilder(parentSpan.getLocation())
				.append(".").append(this.childSpanx.incrementAndGet())
				.toString();
		String newRpcid = new StringBuilder(parentSpan.getTraceId())
				.append("-").append(this.childRpcIdx.incrementAndGet())
				.toString();

		Spanner child = new Spanner(parentSpan.getTraceId(), newRpcid, newSpan,
				spanTypeEnum, isTopSpan, parentSpan);
		spanThreadLocal.set(child);
		return child;
	}

}
