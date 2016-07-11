package com.share.commons.log.impl;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class Formater {

	public Formater() {
	}

	String format(LoggingEvent event) {
		return String.valueOf(event.getMessage());
	}
	static class LayoutFormater extends Formater {
		private Layout layout;

		public LayoutFormater(Layout layout) {
			this.layout = layout;
		}

		@Override
		String format(LoggingEvent event) {
			return layout.format(event);
		}
	}
}
