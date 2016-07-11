package com.share.commons.log.model;

public enum LogStatus {
	success {
		@Override
		public String toStr() {
			return "0";
		}
	},
	error {
		@Override
		public String toStr() {
			return "1";
		}
	};
	public abstract String toStr();
}
